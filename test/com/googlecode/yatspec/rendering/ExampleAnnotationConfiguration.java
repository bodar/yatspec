package com.googlecode.yatspec.rendering;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@CustomRenderConfiguration({
        @RenderMapping(type = ExampleAnnotationConfiguration.CustomType.class, renderer = ExampleAnnotationConfiguration.RendererWhichAlwaysRetunsTheSameString.class),
        @RenderMapping(type = ExampleAnnotationConfiguration.CustomType.class, renderer = ExampleAnnotationConfiguration.RendererWhichAlwaysRetunsTheSameString.class)
})
public class ExampleAnnotationConfiguration {
    @Test
    public void passes() {
        assertThat(true, is(true));
    }

    public static final class CustomType {}

    public static final class RendererWhichAlwaysRetunsTheSameString implements Renderer<CustomType> {
        public static final String THE_SAME_STRING = "ANY_STRING";

        public String render(CustomType customType) throws Exception {
            return THE_SAME_STRING;
        }
    }
}
