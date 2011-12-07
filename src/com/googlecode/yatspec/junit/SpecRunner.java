package com.googlecode.yatspec.junit;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.yatspec.rendering.*;
import com.googlecode.yatspec.rendering.html.HtmlIndexRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.TestResult;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.googlecode.yatspec.state.givenwhenthen.WithTestState;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.Creator.create;
import static java.lang.Class.forName;
import static java.lang.System.getProperty;


public class SpecRunner extends TableRunner {
    public static final String OUTPUT_DIR = "yatspec.output.dir";
    public static final String RESULT_RENDER = "yatspec.result.renderer";
    public static final String SCENARIO_NAME_RENDERER = "yatspec.scenario.name.renderer";
    public static final String INDEX_ENABLE = "yatspec.index.enable";
    public static final String INDEX_RENDER = "yatspec.index.renderer";

    public static void setOutputDir(File directory) {
        System.setProperty(OUTPUT_DIR, directory.getPath());
    }

    public static void setResultRenderer(Class<? extends ContentRenderer<Result>> aClass) {
        System.setProperty(RESULT_RENDER, aClass.getName());
    }

    public static void setIndexRenderer(Class<? extends ContentRenderer<Index>> aClass) {
        enableIndex();
        System.setProperty(INDEX_RENDER, aClass.getName());
    }

    public static void enableIndex() {
        System.setProperty(INDEX_ENABLE, "true");
    }

    private final static Index index = new Index();
    private final Result testResult;
    private Scenario currentScenario;

    public SpecRunner(Class<?> klass) throws org.junit.runners.model.InitializationError {
        super(klass);
        testResult = new TestResult(klass);
    }

    @Override
    protected Object createTest() throws Exception {
        final Object test = super.createTest();
        if (test instanceof WithCustomRendering) {
            testResult.mergeCustomRenderers((((WithCustomRendering) test).getCustomRenderers()));
        }
        if (test instanceof WithCustomHeaderContent) {
            testResult.mergeCustomHeaderContent((((WithCustomHeaderContent) test).getCustomHeaderContent()));
        }
        return test;
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return sequence(super.computeTestMethods()).filter(isNotEvaluateMethod()).toList();
    }

    private static Predicate<FrameworkMethod> isNotEvaluateMethod() {
        return new Predicate<FrameworkMethod>() {
            public boolean matches(FrameworkMethod method) {
                return !method.getName().equals("evaluate");
            }
        };
    }

    @Override
    public void run(RunNotifier notifier) {
        final SpecListener listener = new SpecListener();
        notifier.addListener(listener);
        super.run(notifier);
        notifier.removeListener(listener);
        try {
            File ouputDirectory = getOuputDirectory();
            File file = new ContentWriter<Result>(ouputDirectory, getResultRenderer(), true).write(testResult);
            index.put(file, testResult);
            if (indexEnabled()) {
                new ContentWriter<Index>(ouputDirectory, getIndexRenderer(), false).write(index);
            }
        } catch (Exception e) {
            System.out.println("Error while writing HTML");
            e.printStackTrace(System.out);
        }
    }

    private boolean indexEnabled() {
        return Boolean.parseBoolean(getProperty(INDEX_ENABLE));
    }

    private ContentRenderer<Result> getResultRenderer() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        return create(forName(getProperty(RESULT_RENDER, HtmlResultRenderer.class.getName())));
    }

    private ContentRenderer<Index> getIndexRenderer() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        return create(forName(getProperty(INDEX_RENDER, HtmlIndexRenderer.class.getName())));
    }

    private File getOuputDirectory() {
        return new File(getProperty(OUTPUT_DIR, getProperty("java.io.tmpdir")));
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        final Statement statement = super.methodInvoker(method, test);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                currentScenario = testResult.getScenario(method.getName());

                if (test instanceof WithTestState) {
                    TestState testState = ((WithTestState) test).testState();
                    currentScenario.setTestState(testState);
                }
                statement.evaluate();
            }
        };
    }

    private boolean isInTest() {
        return currentScenario != null;
    }

    private final class SpecListener extends RunListener {

        @Override
        public void testFailure(Failure failure) throws Exception {
            if (isInTest()) {
                currentScenario.setException(failure.getException());
            }
        }
    }
}
