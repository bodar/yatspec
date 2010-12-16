package com.googlecode.yatspec.state.givenwhenthen;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static com.googlecode.yatspec.state.givenwhenthen.StateExtractors.getXpathValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StateExtractorsTest {

    private static final String NAME = "Fred";
    private static final String KEY = "Key";

    @Test
    public void shouldExtractValue() throws Exception {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, NAME);
        assertThat(StateExtractors.getValue(KEY, String.class).execute(inputAndOutputs), is(NAME));
    }

    @Test
    public void shouldExtractXpathElement() throws Exception {
        Document document =  document("<People><Person><Name>" + NAME + "</Name></Person></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);
        assertThat(getXpathValue(KEY, "//People/Person/Name").execute(inputAndOutputs), is(NAME));
    }

    @Test
    public void shouldExtractXpathAttribute() throws Exception {
        Document document = document("<People><Person Name=\"" + NAME + "\"/></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);
        assertThat(getXpathValue(KEY, "//People/Person/@Name").execute(inputAndOutputs), is(NAME));
    }

    private Document document(String xml) throws JDOMException, IOException {
        return new SAXBuilder().build(new StringReader(xml));
    }

}
