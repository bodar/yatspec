package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.Result;

import java.util.HashMap;
import java.util.Map;

public class CustomRendererRegistry {
    public static final Map<Class<?>, Renderer> renderers = new HashMap<Class<?>, Renderer>();

    public CustomRendererRegistry() {
    }

    public void registerCustomRenderers(EnhancedStringTemplateGroup group, Result result) {
        registerStaticRenderers(group);
        registerAnnotationsRenderers(group, result);
    }

    private void registerStaticRenderers(EnhancedStringTemplateGroup group) {
        for (Class<?> rendererType : renderers.keySet()) {
            group.registerRenderer(rendererType, renderers.get(rendererType));
        }
    }

    private void registerAnnotationsRenderers(EnhancedStringTemplateGroup group, Result result) {
        final CustomRenderConfiguration configuration = result.getTestClass().getAnnotation(CustomRenderConfiguration.class);
        if (configuration != null) {
            for (RenderMapping renderMapping : configuration.value()) {
                group.registerRenderer(renderMapping.type(), instantiateRenderer(renderMapping));
            }
        }
    }

    private Renderer instantiateRenderer(RenderMapping renderMapping) {
        try {
            return renderMapping.renderer().newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}