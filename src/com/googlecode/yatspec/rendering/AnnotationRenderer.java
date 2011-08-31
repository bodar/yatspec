package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;

import java.lang.annotation.Annotation;

final class AnnotationRenderer implements AttributeRenderer {
    private final EnhancedStringTemplateGroup stringTemplateGroup;

    public AnnotationRenderer(EnhancedStringTemplateGroup stringTemplateGroup) {
        this.stringTemplateGroup = stringTemplateGroup;
    }

    @Override
    public String toString(Object object) {
        Class<? extends Annotation> aClass = ((Annotation) object).annotationType();
        return stringTemplateGroup.getAttributeRenderer(aClass).toString(object);
    }

    @Override
    public String toString(Object object, String formatName) {
        return toString(object);
    }
}