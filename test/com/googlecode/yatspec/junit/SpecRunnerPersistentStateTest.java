package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.fixture.RandomFixtures;
import com.googlecode.yatspec.state.ResultMetadata;
import com.googlecode.yatspec.state.ResultMetadataOnly;
import com.googlecode.yatspec.state.Status;
import com.googlecode.yatspec.state.TestMethodMetadataOnly;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.io.*;
import java.util.*;

public class SpecRunnerPersistentStateTest {

    private String persistentDataDirPropertyValue;
    private File persistentDataDir;
    private RunNotifier notifier;
    private static final List<ResultMetadata> resultsSeenByMetadataListener = new ArrayList<>();

    @Before
    public void setUp() throws IOException {
        resultsSeenByMetadataListener.clear();
        notifier = new RunNotifier();
        persistentDataDir = File.createTempFile(SpecRunnerPersistentStateTest.class.getSimpleName(), "");
        persistentDataDir.delete();
        persistentDataDir.mkdir();
        persistentDataDirPropertyValue = System.getProperty(SpecRunner.DATA_DIR);
        System.setProperty(SpecRunner.DATA_DIR, persistentDataDir.getAbsolutePath());
    }

    @After
    public void tearDown() {
        if (persistentDataDirPropertyValue == null) {
            System.clearProperty(SpecRunner.DATA_DIR);
        } else {
            System.setProperty(SpecRunner.DATA_DIR, persistentDataDirPropertyValue);
        }
        for (File file : persistentDataDir.listFiles()) {
            file.delete();
        }
        persistentDataDir.delete();
    }

    @Test
    public void persistentStateIsSaved() throws Exception {
        SpecRunner specRunner = new SpecRunner(HappyPathTest.class);
        specRunner.run(notifier);

        List<String> directoryList = Arrays.asList(persistentDataDir.list());
        Assert.assertThat(directoryList, Matchers.hasSize(1));
        Assert.assertThat(directoryList.get(0), Matchers.endsWith(HappyPathTest.class.getSimpleName() + SpecRunner.SERIALIZED_DATA_EXTENSION));

        try (FileInputStream fis = new FileInputStream(new File(persistentDataDir, directoryList.get(0)));
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object o = ois.readObject();
            Assert.assertThat(o, Matchers.instanceOf(ResultMetadataOnly.class));
        }
    }

    @Test
    public void persistentStateIsNotSavedWhenSystemPropertyIsNotSpecified() throws InitializationError {
        System.clearProperty(SpecRunner.DATA_DIR);

        SpecRunner specRunner = new SpecRunner(HappyPathTest.class);
        specRunner.run(notifier);

        List<String> directoryList = Arrays.asList(persistentDataDir.list());
        Assert.assertThat(directoryList, Matchers.hasSize(0));
    }

    @Test
    public void listenerReceivesCompleteViewOfPersistentState() throws Exception {
        // write some stuff to results dir
        ResultMetadataOnly bogusResult1 = createBogusResult();
        writeResultToDataDir(bogusResult1);

        // run a test with a results listener
        new SpecRunner(HappyPathTestWithCustomResultListeners.class).run(notifier);

        // verify that results listener gets both the result written to the data dir and the result from the real test
        Assert.assertThat(resultsSeenByMetadataListener, Matchers.hasSize(2));
        Assert.assertThat(resultsSeenByMetadataListener, Matchers.hasItem(Matchers.equalTo(bogusResult1)));
        Assert.assertThat(resultsSeenByMetadataListener, Matchers.hasItem(Matchers.hasProperty("testClassName", Matchers.equalTo(HappyPathTestWithCustomResultListeners.class.getName()))));

        // verify that real-test result was written to the directory as well
        Assert.assertThat(Arrays.asList(persistentDataDir.list()), Matchers.hasSize(2));

        resultsSeenByMetadataListener.clear();

        // write more results to results dir
        ResultMetadataOnly bogusResult2 = createBogusResult();
        writeResultToDataDir(bogusResult2);
        ResultMetadataOnly bogusResult3 = createBogusResult();
        writeResultToDataDir(bogusResult3);
        ResultMetadataOnly bogusResult4 = createBogusResult();
        writeResultToDataDir(bogusResult4);

        // run another real test with a results listener
        new SpecRunner(HappyPathTest2WithCustomResultListeners.class).run(notifier);

        // verify that all the above get fed to the results listener
        Assert.assertThat(resultsSeenByMetadataListener, Matchers.hasSize(4));
        Assert.assertThat(resultsSeenByMetadataListener, Matchers.hasItem(Matchers.equalTo(bogusResult2)));
        Assert.assertThat(resultsSeenByMetadataListener, Matchers.hasItem(Matchers.equalTo(bogusResult3)));
        Assert.assertThat(resultsSeenByMetadataListener, Matchers.hasItem(Matchers.equalTo(bogusResult4)));
        Assert.assertThat(resultsSeenByMetadataListener, Matchers.hasItem(Matchers.hasProperty("testClassName", Matchers.equalTo(HappyPathTest2WithCustomResultListeners.class.getName()))));

        // verify that real-test result was written to the directory alongside all the other ones
        Assert.assertThat(Arrays.asList(persistentDataDir.list()), Matchers.hasSize(6));
    }


    private static int bogusDataCounter = 0;
    private ResultMetadataOnly createBogusResult() {
        bogusDataCounter++;
        return new ResultMetadataOnly(String.format("Bogus Test %d", bogusDataCounter),
                "com.googlecode.yatspec.junit",
                String.format("com.googlecode.yatspec.junit.BogusTest%d", bogusDataCounter),
                Collections.singletonList(new TestMethodMetadataOnly(
                    "bogusTestMethod",
                    "Bogus Test Method",
                    "Bogus Test Method",
                    Integer.toString(bogusDataCounter),
                    "com.googlecode.yatspec.junit",
                        String.format("com.googlecode.yatspec.junit.BogusTest%d", bogusDataCounter),
                        Status.Passed,
                        Collections.emptyList()
                )),
                Collections.emptyList());
    }

    private void writeResultToDataDir(ResultMetadataOnly result) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(new File(persistentDataDir, result.getTestClassName() + SpecRunner.SERIALIZED_DATA_EXTENSION));
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(result);
        }
    }


    public static class HappyPathTest {
        @Test
        public void happyPathTest() {
            Assert.assertTrue(true);
        }
    }

    public static class HappyPathTestWithCustomResultListeners implements WithCustomResultListeners {
        @Test
        public void happyPathTest() {
            Assert.assertTrue(true);
        }

        @Override
        public Iterable<SpecResultListener> getResultListeners() throws Exception {
            // ???????
            return Collections.singleton(new SpecResultMetadataListener() {
                @Override
                public void completeMetadata(File yatspecOutputDir, Collection<ResultMetadata> results) throws Exception {
                    resultsSeenByMetadataListener.addAll(results);
                }
            });
        }
    }

    public static class HappyPathTest2WithCustomResultListeners extends HappyPathTestWithCustomResultListeners {
    }

}
