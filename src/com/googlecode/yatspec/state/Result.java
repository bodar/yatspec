package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Option;

import java.util.List;

public interface Result extends Notable {

    List<TestMethod> getTestMethods() throws Exception;

    Class<?> getTestClass();

    Scenario getScenario(String name) throws Exception;

    String getName();

    String getPackageName();

    <T> Option<T> safeCastTestInstanceTo(Class<T> ifInstanceOf);
}