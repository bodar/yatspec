package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.TestResult;
import org.junit.Test;

import static com.googlecode.yatspec.rendering.ExampleAnnotationConfiguration.RendererWhichAlwaysRetunsTheSameString.THE_SAME_STRING;
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
    public void supportsCustomRenderingOfScenarioLogs() throws Exception{
        // setup
        final String customRenderedText = "some crazy and likely random string that wouldn't appear in the html";
        CustomRendererRegistry.renderers.put(RenderedType.class, new DefaultReturningRenderer(customRenderedText));
        TestResult result = aTestResultWithCustomRenderTypeAddedToScenarioLogs();
        // execute
        String html = new ResultRenderer().render(result);

        // verify
        assertThat(html, containsString(customRenderedText));
    }

    @Test
    public void supportCustomRendererUsingAnnotations() throws Exception {
        final TestResult testResult = new TestResult(ExampleAnnotationConfiguration.class);
        addToTestLogs(testResult, new ExampleAnnotationConfiguration.CustomType());
        final String html = new ResultRenderer().render(testResult);
        assertThat(html, containsString(THE_SAME_STRING));
    }

    private TestResult aTestResultWithCustomRenderTypeAddedToScenarioLogs() throws Exception {
        TestResult result = new TestResult(getClass());
        addToTestLogs(result, new RenderedType());
        return result;
    }

    private void addToTestLogs(TestResult result, Object thingToBeCustomRendered) throws Exception {
        result.getTestMethods().get(0).getScenarios().get(0).getLogs().put("custom rendered thing", thingToBeCustomRendered);
    }


    private static class RenderedType{}

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
