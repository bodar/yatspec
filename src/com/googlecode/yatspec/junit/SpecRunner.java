package com.googlecode.yatspec.junit;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.yatspec.rendering.ResultWriter;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.TestResult;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.io.File;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;


public class SpecRunner extends TableRunner {
    public static final String OUTPUT_DIR = "yatspec.output.dir";
    private final Result testResult;
    private Scenario currentScenario;

    public SpecRunner(Class<?> klass) throws org.junit.runners.model.InitializationError {
        super(klass);
        testResult = new TestResult(klass);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return sequence(super.computeTestMethods()).filter(new Predicate<FrameworkMethod>() {
            public boolean matches(FrameworkMethod method) {
                return !method.getName().equals("evaluate"); 
            }
        }).toList();
    }

    @Override
    public void run(RunNotifier notifier) {
        final SpecListener listener = new SpecListener();
        notifier.addListener(listener);
        super.run(notifier);
        notifier.removeListener(listener);
        try {
            new ResultWriter(getOuputDirectory()).write(testResult);
        } catch (Exception e) {
            System.out.println("Error while writing HTML");
            e.printStackTrace(System.out);
        }
    }

    private File getOuputDirectory() {
        return new File(System.getProperty(OUTPUT_DIR, System.getProperty("java.io.tmpdir")));
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        final Statement statement = super.methodInvoker(method, test);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                currentScenario = testResult.getScenario(method.getName());

                if(test instanceof TestState){
                    TestState testState = (TestState) test;
                    currentScenario.setTestState(testState);
                }
                statement.evaluate();
            }
        };
    }

    private class SpecListener extends RunListener {
        @Override
        public void testFailure(Failure failure) throws Exception {
            currentScenario.setException(failure.getException());
        }
    }
}
