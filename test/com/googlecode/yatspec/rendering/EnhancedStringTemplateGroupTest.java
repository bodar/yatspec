package com.googlecode.yatspec.rendering;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class EnhancedStringTemplateGroupTest {
    private static final String VALUE = "A";
    private static final String OTHER_VALUE = "B";
    private EnhancedStringTemplateGroup stringTemplateGroup = new EnhancedStringTemplateGroup("name");

    @Before
    public void setUp() {
        stringTemplateGroup.registerDefaultRenderer(new DelegateAttributeRenderer(VALUE));
    }

    @Test
    public void shouldReturnDefaultRendererIfNoneFound() {
        assertThat(stringTemplateGroup.getAttributeRenderer(String.class).toString(null), equalTo(VALUE));
    }

    @Test
    public void shouldReturnClassRenderer() {
        stringTemplateGroup.registerRenderer(String.class, new DelegateAttributeRenderer(OTHER_VALUE));

        assertThat(stringTemplateGroup.getAttributeRenderer(String.class).toString(null), equalTo(OTHER_VALUE));
    }

    @Test
    public void shouldReturnAnnotationRenderer() throws NoSuchMethodException {
        stringTemplateGroup.registerRenderer(Test.class, new DelegateAttributeRenderer(OTHER_VALUE));

        assertThat(stringTemplateGroup.getAttributeRenderer(Test.class).toString(null), equalTo(OTHER_VALUE));
    }
}