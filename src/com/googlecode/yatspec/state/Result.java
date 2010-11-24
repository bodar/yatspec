package com.googlecode.yatspec.state;

import com.googlecode.yatspec.rendering.Renderer;

import java.util.List;
import java.util.Map;

public interface Result {

    List<TestMethod> getTestMethods() throws Exception;

    Class<?> getTestClass();

    Scenario getScenario(String name) throws Exception;

    String getName();

    String getNotes();

    void mergeCustomRenderers(Map<Class, Renderer> renderers);

    Map<Class, Renderer> getCustomRenderers();
}
