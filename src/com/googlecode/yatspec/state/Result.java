package com.googlecode.yatspec.state;

import java.util.List;

public interface Result extends ResultMetadata {

    List<TestMethod> getTestMethods() throws Exception;

    Class<?> getTestClass();

    Scenario getScenario(String name) throws Exception;

}