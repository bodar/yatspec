package com.googlecode.yatspec.state;

import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.WithCustomHeaderContent;
import com.googlecode.yatspec.rendering.WithCustomRendering;

import java.util.List;
import java.util.Map;

public interface Result extends Notable, WithCustomRendering, WithCustomHeaderContent {

    List<TestMethod> getTestMethods() throws Exception;

    Class<?> getTestClass();

    Scenario getScenario(String name) throws Exception;

    String getName();

    void mergeCustomRenderers(Map<Class, Renderer> renderers);

    void mergeCustomHeaderContent(Content customHeaderContent);
}