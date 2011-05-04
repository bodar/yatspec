package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;
import org.junit.Test;

import static com.googlecode.yatspec.rendering.Annotations.notesProxyClass;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class EnhancedStringTemplateGroupTest {
    private EnhancedStringTemplateGroup stringTemplateGroup = new EnhancedStringTemplateGroup("name");

    @Test
    public void shouldReturnDefaultRendererIfNoneFound() {
        AttributeRenderer defaultRenderer = new XmlStringRenderer();
        stringTemplateGroup.registerDefaultRenderer(defaultRenderer);
        assertThat(stringTemplateGroup.getAttributeRenderer(String.class), sameInstance(defaultRenderer));
    }

    @Test
    public void shouldPreserveDefaultBehaviour() {
        AttributeRenderer stubRenderer = new XmlStringRenderer();
        stringTemplateGroup.registerRenderer(String.class, stubRenderer);
        assertThat(stringTemplateGroup.getAttributeRenderer(String.class), sameInstance(stubRenderer));
    }

    @Test
    public void shouldReturnAnnotationRenderer() throws Exception {
        assertThat(stringTemplateGroup.getAttributeRenderer(notesProxyClass()), instanceOf(AnnotationRenderer.class));
    }
}