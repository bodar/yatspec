package com.googlecode.yatspec.state.givenwhenthen;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import static com.googlecode.yatspec.state.givenwhenthen.StateExtractors.getXpathValue;
import static com.googlecode.yatspec.state.givenwhenthen.StateExtractors.getXpathValues;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class StateExtractorsTest {

    private static final String NAME = "Fred";
    private static final String ANOTHER_NAME = "Joe";
    private static final String KEY = "Key";

    @Test
    public void shouldExtractValue() throws Exception {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, NAME);
        assertThat(StateExtractors.getValue(KEY, String.class).execute(inputAndOutputs), is(NAME));
    }

    @Test
    public void shouldExtractValues() throws Exception {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, Arrays.asList(NAME, NAME));
        assertThat(StateExtractors.<Collection<String>, String>getValues(KEY, String.class).execute(inputAndOutputs), hasItem(NAME));
    }

    @Test
    public void shouldExtractXpathElement() throws Exception {
        Document document =  document("<People><Person><Name>" + NAME + "</Name></Person></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);
        assertThat(getXpathValue(KEY, "//People/Person/Name").execute(inputAndOutputs), is(NAME));
    }

    @Test
    public void shouldExtractMultipleSiblingXPathElements() throws Exception {
        Document document =  document("<People><Person>"+"<Name>" + NAME + "</Name>"+"<Name>" + ANOTHER_NAME + "</Name>"+"</Person></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);
        assertThat(StateExtractors.getXpathValues(KEY, "//People/Person/Name").execute(inputAndOutputs), is(asList(NAME, ANOTHER_NAME)));
    }

    @Test
    public void shouldExtractXpathAttribute() throws Exception {
        Document document = document("<People><Person Name=\"" + NAME + "\"/></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);
        assertThat(getXpathValue(KEY, "//People/Person/@Name").execute(inputAndOutputs), is(NAME));
    }

    @Test
    public void shouldExtractSiblingXpathAttribute() throws Exception {
        Document document = document("<People><Person Name=\"" + NAME + "\"/><Person Name=\"" + ANOTHER_NAME + "\"/></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);
        assertThat(getXpathValues(KEY, "//People/Person/@Name").execute(inputAndOutputs), is(asList(NAME, ANOTHER_NAME)));
    }

    @Test(expected=IllegalStateException.class)
    public void shouldThrowExceptionWhenXPathIsNotFound() throws Exception {
        Document document = document("<People><Person Name=\"" + NAME + "\"/></People>");
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add(KEY, document);
        getXpathValue(KEY, "//doesNotExist").execute(inputAndOutputs);
    }

    private Document document(String xml) throws JDOMException, IOException {
        return new SAXBuilder().build(new StringReader(xml));
    }

}
