package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.StringTemplateGroup;

import java.util.Map;

import static java.lang.reflect.Proxy.isProxyClass;

final class EnhancedStringTemplateGroup extends StringTemplateGroup {
    private AttributeRenderer defaultRenderer;

    public EnhancedStringTemplateGroup(String name) {
        super(name);
    }

    public void registerDefaultRenderer(AttributeRenderer renderer) {
        defaultRenderer = renderer;
    }

    public <T> void registerRenderer(Class<T> attributeClassType, Renderer<T> renderer) {
        super.registerRenderer(attributeClassType, new RendererAdapter<T>(renderer));
    }

    @SuppressWarnings({"unchecked"})
    public void registerRenderers(Map<Class, Renderer> renderers) {
        for (Map.Entry<Class, Renderer> rendererType : renderers.entrySet()) {
            registerRenderer(rendererType.getKey(), rendererType.getValue());
        }
    }

    @Override
    public AttributeRenderer getAttributeRenderer(Class attributeClassType) {
        AttributeRenderer attributeRenderer = super.getAttributeRenderer(attributeClassType);
        if (attributeRenderer == null) {
            return isAnnotationProxy(attributeClassType) ? new AnnotationRenderer(this) : defaultRenderer;
        }
        return attributeRenderer;
    }

    private static boolean isAnnotationProxy(Class attributeClassType) {
        return isProxyClass(attributeClassType);
    }
}

