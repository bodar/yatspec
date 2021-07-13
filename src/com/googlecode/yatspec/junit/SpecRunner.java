package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.*;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.System.getProperty;

public class SpecRunner extends TableRunner {
    public static final String OUTPUT_DIR = "yatspec.output.dir";
    public static final String DATA_DIR = "yatspec.data.dir";
    private static final Set<String> resultsAlreadySeen = new HashSet<>();
    public static final String SERIALIZED_DATA_EXTENSION = ".ser";
    private final Result testResult;
    private Map<String, Scenario> currentScenario = new HashMap<String, Scenario>();

    public SpecRunner(Class<?> klass) throws org.junit.runners.model.InitializationError {
        super(klass);
        testResult = new TestResult(klass);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return super.computeTestMethods().stream().filter(method -> !method.getName().equals("evaluate")).collect(Collectors.toList());
    }

    private WithCustomResultListeners listeners = new DefaultResultListeners();

    @Override
    protected Object createTest() throws Exception {
        Object instance = super.createTest();
        if (instance instanceof WithCustomResultListeners) {
            listeners = (WithCustomResultListeners) instance;
        } else {
            listeners = new DefaultResultListeners();
        }
        return instance;
    }

    @Override
    public void run(RunNotifier notifier) {
        final SpecListener listener = new SpecListener();
        notifier.addListener(listener);
        super.run(notifier);
        notifier.removeListener(listener);

        Iterable<SpecResultListener> resultListeners;
        try {
            resultListeners = listeners.getResultListeners();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (dataDirectory() != null) {
            updateTestResults(dataDirectory(), testResult, resultListeners);
        }

        try {
            for (SpecResultListener resultListener : resultListeners) {
                resultListener.complete(outputDirectory(), testResult);
            }
        } catch (Exception e) {
            System.out.println("Error while writing yatspec output");
            e.printStackTrace(System.out);
        }
    }

    private static void updateTestResults(File dataDirectory, Result result, Iterable<SpecResultListener> resultListeners) {
        ResultMetadataOnly resultMetadataOnly = ResultMetadataOnly.fromResult(result);
        String testClassName = resultMetadataOnly.getTestClassName();
        resultsAlreadySeen.add(testClassName);

        {
            dataDirectory.mkdirs();
            File tmpFile = new File(dataDirectory, testClassName + SERIALIZED_DATA_EXTENSION + ".tmp");
            try (FileOutputStream fos = new FileOutputStream(tmpFile); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(resultMetadataOnly);
            } catch (IOException e) {
                throw new RuntimeException("error serializing Yatspec test data: " + e.getStackTrace(), e);
            }
            File finalFile = new File(dataDirectory, testClassName + SERIALIZED_DATA_EXTENSION);
            tmpFile.renameTo(finalFile);
        }

        Set<SpecResultListener> metadataListeners = StreamSupport.stream(resultListeners.spliterator(), false).filter(x -> x instanceof SpecResultMetadataListener).collect(Collectors.toSet());
        if (!metadataListeners.isEmpty()) {
            Set<ResultMetadata> metadata = new HashSet<>();
            for (File file : dataDirectory.listFiles()) {
                String fileName = file.getName();
                if (!fileName.endsWith(SERIALIZED_DATA_EXTENSION)) {
                    continue;
                }
                String className = fileName.substring(0, fileName.length() - SERIALIZED_DATA_EXTENSION.length());
                if (resultsAlreadySeen.contains(className)) {
                    continue;
                }

                try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis)) {
                    metadata.add((ResultMetadata) ois.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("error deserializing Yatspec test data: " + e.getStackTrace(), e);
                }
            }
            try {
                for (SpecResultListener metadataListener : metadataListeners) {
                    ((SpecResultMetadataListener) metadataListener).completeMetadata(outputDirectory(), metadata);
                }
            } catch (Exception e) {
                System.out.println("Error while writing yatspec output");
                e.printStackTrace(System.out);
            }

            resultsAlreadySeen.addAll(metadata.stream().map(ResultMetadata::getTestClassName).collect(Collectors.toSet()));
        }
    }

    public static File outputDirectory() {
        return new File(getProperty(OUTPUT_DIR, getProperty("java.io.tmpdir")));
    }

    private static File dataDirectory() {
        String dataDirPath = getProperty(DATA_DIR);
        if (dataDirPath == null || dataDirPath.length() == 0) {
            return null;
        } else {
            return new File(dataDirPath);
        }
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        final Statement statement = super.methodInvoker(method, test);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final String fullyQualifiedTestMethod = test.getClass().getCanonicalName() + "." + method.getName();
                final Scenario scenario = testResult.getScenario(method.getName());
                currentScenario.put(fullyQualifiedTestMethod, scenario);

                if (test instanceof WithTestState) {
                    TestState testState = ((WithTestState) test).testState();
                    currentScenario.get(fullyQualifiedTestMethod).setTestState(testState);
                }
                statement.evaluate();
            }
        };
    }

    private final class SpecListener extends RunListener {
        @Override
        public void testFailure(Failure failure) throws Exception {
            String fullyQualifiedTestMethod = failure.getDescription().getClassName() + "." + failure.getDescription().getMethodName();
            if (currentScenario.get(fullyQualifiedTestMethod) != null) currentScenario.get(fullyQualifiedTestMethod).setException(failure.getException());
        }
    }
}
