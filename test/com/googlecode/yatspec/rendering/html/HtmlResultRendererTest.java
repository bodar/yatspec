package com.googlecode.yatspec.rendering.html;

import com.googlecode.totallylazy.Strings;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.state.Scenario;
import com.googlecode.yatspec.state.TestResult;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

public class HtmlResultRendererTest {

    public static final String CUSTOM_RENDERED_TEXT = "some crazy and likely random string that wouldn't appear in the html";

    @Test
    public void loadsTemplateOffClassPath() throws Exception {
        // setup
        TestResult result = new TestResult(this.getClass());

        // execute
        String html = new HtmlResultRenderer().render(result);

        // verify
        assertThat(html, is(not(nullValue())));
    }

    @Test
    public void supportsCustomRenderingOfScenarioLogs() throws Exception {
        // setup
        TestResult result = aTestResultWithCustomRenderTypeAddedToScenarioLogs();
        // execute
        String html = new HtmlResultRenderer().
                withCustomRenderer(RenderedType.class, new DefaultReturningRenderer(CUSTOM_RENDERED_TEXT)).
                render(result);

        // verify
        assertThat(html, containsString(CUSTOM_RENDERED_TEXT));
    }

    @Test
    public void supportsCustomHeaderContent() throws Exception {

        TestResult result = new TestResult(getClass());

        String html = new HtmlResultRenderer().
                withCustomHeaderContent(new Content(getClass().getResource("CustomHeaderContent.html"))).
                render(result);

        assertThat(html, containsString(Strings.toString(getClass().getResource("CustomHeaderContent.html").openStream())));
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

        public DefaultReturningRenderer(final String rendererOutput) {
            this.rendererOutput = rendererOutput;
        }

        public String render(RenderedType renderedType) throws Exception {
            return rendererOutput;
        }
    }
}
