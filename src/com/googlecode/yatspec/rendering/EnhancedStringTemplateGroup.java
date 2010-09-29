package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.StringTemplateGroup;

public class EnhancedStringTemplateGroup extends StringTemplateGroup {
    private AttributeRenderer defaultRenderer;

    public EnhancedStringTemplateGroup(String name) {
        super(name);
    }

    public void registerDefaultRenderer(AttributeRenderer renderer) {
        defaultRenderer = renderer;
    }

    public <T> void registerDefaultRenderer(Renderer<T> renderer) {
        defaultRenderer = new RendererAdapter<T>(renderer);
    }

    public <T> void registerRenderer(Class<T> attributeClassType, Renderer<T> renderer) {
        super.registerRenderer(attributeClassType, new RendererAdapter<T>(renderer));
    }

    @Override
    public AttributeRenderer getAttributeRenderer(Class attributeClassType) {
        AttributeRenderer attributeRenderer = super.getAttributeRenderer(attributeClassType);
        if( attributeRenderer == null) {
            return defaultRenderer;
        }
        return attributeRenderer;
    }
}

