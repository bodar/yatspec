package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.TestResult;
import org.junit.Test;

import java.io.File;

public class IndexModelTest {
    @Test
    public void should() throws Exception {
        Index index = new Index().
                put(new File("firstTestClass.html"), new TestResult(FirstTestClass.class)).
                put(new File("firstTestClass.html"), new TestResult(FirstTestClass.class));
    }

    private static class FirstTestClass{
        @Test
        public void firstTestOfFirstTestClass(){}
        @Test
        public void secondTestOfFirstTestClass(){}
    }
    private static class SecondTestClass{
        @Test
        public void firstTestOfSecondTestClass(){}
        @Test
        public void secondTestOfSecondTestClass(){}
    }
}
