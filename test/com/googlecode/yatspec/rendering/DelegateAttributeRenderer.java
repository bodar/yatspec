package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;

final class DelegateAttributeRenderer implements AttributeRenderer {
    private final String value;

    public DelegateAttributeRenderer(final String value) {
        this.value = value;
    }

    @Override
    public String toString(Object o) {
        return value;
    }

    @Override
    public String toString(Object object, String formatName) {
        return toString(object);
    }
}
