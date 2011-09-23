package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.TestResult;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class ResultRendererTest {
    @Test
    public void loadsTemplateOffClassPath() throws Exception {
        // setup
        TestResult result = new TestResult(this.getClass());

        // execute
        String html = new ResultRenderer().render(result);

        // verify
        assertThat(html, is(not(nullValue())));
    }

    @Test
    public void supportsCustomRenderingOfScenarioLogs() throws Exception {
        // setup
        final String customRenderedText = "some crazy and likely random string that wouldn't appear in the html";
        TestResult result = aTestResultWithCustomRenderTypeAddedToScenarioLogs();
        result.mergeCustomRenderers(new HashMap<Class, Renderer>() {{
            put(RenderedType.class, new DefaultReturningRenderer(customRenderedText));
        }});
        // execute
        String html = new ResultRenderer().render(result);

        // verify
        assertThat(html, containsString(customRenderedText));
    }

    @Test
    public void supportsCustomHeaderContent() throws Exception {

        TestResult result = new TestResult(getClass());

        result.mergeCustomHeaderContent(new Content(getClass().getResource("CustomHeaderContent.html")));


        String html = new ResultRenderer().render(result);

        assertThat(html, containsString("walrus"));

    }


    private TestResult aTestResultWithCustomRenderTypeAddedToScenarioLogs() throws Exception {
        TestResult result = new TestResult(getClass());
        addToCapturedInputsAndOutputs(result, new RenderedType());
        return result;
    }

    private void addToCapturedInputsAndOutputs(TestResult result, Object thingToBeCustomRendered) throws Exception {
        Scenario scenario = result.getTestMethods().get(0).getScenarios().get(0);
        TestState testState = new TestState();
        testState.capturedInputAndOutputs.add("custom rendered thing", thingToBeCustomRendered);
        scenario.setTestState(testState);
    }

    private static class RenderedType {
    }

    private class DefaultReturningRenderer implements Renderer<RenderedType> {
        private String rendererOutput;

        private DefaultReturningRenderer() {
            this("default string");
        }

        public DefaultReturningRenderer(final String rendererOutput) {
            this.rendererOutput = rendererOutput;
        }

        public String render(RenderedType renderedType) throws Exception {
            return rendererOutput;
        }

    }
}
