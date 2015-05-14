package com.googlecode.yatspec.junit;

import com.googlecode.totallylazy.Predicate;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.System.getProperty;

public class SpecRunner extends TableRunner {
    public static final String OUTPUT_DIR = "yatspec.output.dir";
    private final Result testResult;
    private Map<String, Scenario> currentScenario = new HashMap<String, Scenario>();

    public SpecRunner(Class<?> klass) throws org.junit.runners.model.InitializationError {
        super(klass);
        testResult = new TestResult(klass);
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
        try {
            for (SpecResultListener resultListener : listeners.getResultListeners()) {
                resultListener.complete(outputDirectory(), testResult);
            }
        } catch (Exception e) {
            System.out.println("Error while writing yatspec output");
            e.printStackTrace(System.out);
        }
    }

    public static File outputDirectory() {
        return new File(getProperty(OUTPUT_DIR, getProperty("java.io.tmpdir")));
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
