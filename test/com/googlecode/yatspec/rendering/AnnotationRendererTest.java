package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AnnotationRendererTest {
    private static final String RENDERED_VALUE = "test";
    private AttributeRenderer annotationRenderer;

    @Before
    public void setUp() throws Exception {
        EnhancedStringTemplateGroup stringTemplateGroup = new EnhancedStringTemplateGroup("name");
        stringTemplateGroup.registerRenderer(Test.class, new DelegateAttributeRenderer(RENDERED_VALUE));
        annotationRenderer = new AnnotationRenderer(stringTemplateGroup);
    }

    @Test
    public void shouldRenderAnnotation() throws Exception {
        assertThat(annotationRenderer.toString(testAnnotation()), equalTo(RENDERED_VALUE));
    }

    private Object testAnnotation() throws Exception {
        return getClass().getMethod("shouldRenderAnnotation").getAnnotation(Test.class);
    }

}
