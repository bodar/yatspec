package com.googlecode.yatspec.parsing;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextTest {
    @Test
    public void testWordifyOnATextEndingWithASingleUppercaseLetter() {
         assertEquals("Replace a with b", Text.wordify("replaceAWithB;").trim());
    }
}
