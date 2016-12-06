package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.rendering.NotesRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.HyperlinkRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.rendering.html.tagindex.HtmlTagIndexRenderer;
import com.googlecode.yatspec.state.givenwhenthen.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.Math.ceil;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;

@RunWith(SpecRunner.class)
public class CollapsibleMethodsSpecificationExampleTest extends TestState implements WithCustomResultListeners {
    private int inputNumber;
    private int result;

    @Test
    public void complexSectionsOfATestCanBeCollapsedToIncreaseReadability() throws Exception {
        givenANegativeNumber();
        whenICaluclateItsAbsoluteValue();
        thenTheResultValueIsPositive();
    }

    @Test
    public void collapsedMethodsWithIdenticalNameButDifferentParameterTypes() throws Exception {
        givenOneParameterCall(1);
        givenOneParameterCall("1");
        givenTwoParameterCall(1, 1);
        givenTwoParameterCall(1, "234234");
        whenNoParameters();
        thenAnotherOneWithNoParameters();
    }

    @Collapsible
    private void thenTheResultValueIsPositive() {
        assertThat(result, is(greaterThan(0)));
    }

    @Collapsible
    private void whenICaluclateItsAbsoluteValue() {
        result = Math.abs(inputNumber);
    }

    @Collapsible
    private void givenANegativeNumber() {
        inputNumber = -321;
    }

    @Collapsible
    private void givenTwoParameterCall(int i, String s) {
        assertThat("abc", containsString("a"));
        assertThat("abc", containsString("b"));
        assertThat("abc", containsString("c"));
        assertThat("abc", not(containsString("d")));
    }

    @Collapsible
    private void givenTwoParameterCall(int i, int s) {
        assertThat(ceil(1.21), is(2.0));
        assertThat(ceil(1.22), is(2.0));
        assertThat(ceil(1.23), is(2.0));
    }

    @Collapsible
    private void givenOneParameterCall(String s) {
        assertThat(ceil(1.31), is(2.0));
        assertThat(ceil(1.32), is(2.0));
        assertThat(ceil(1.33), is(2.0));
        assertThat(ceil(1.34), is(2.0));
        assertThat(ceil(1.35), is(2.0));
    }

    @Collapsible
    private void givenOneParameterCall(int i) {
        assertThat(ceil(1.41), is(2.0));
        assertThat(ceil(1.42), is(2.0));
    }

    @Collapsible
    private void whenNoParameters() {
        assertThat(ceil(1.51), is(2.0));
        assertThat(ceil(1.52), is(2.0));
        assertThat(ceil(1.53), is(2.0));
    }

    @Collapsible
    private void thenAnotherOneWithNoParameters() {
        assertThat(ceil(1.6), is(2.0));
    }

    public Iterable<SpecResultListener> getResultListeners() throws Exception {
        return sequence(
                new HtmlResultRenderer().
                        withCustomRenderer(Notes.class, new HyperlinkRenderer(new NotesRenderer(), "(?:#)([^\\s]+)", "<a href='http://localhost:8080/pretent-issue-tracking/$1'>$1</a>")),
                new HtmlIndexRenderer(),
                new HtmlTagIndexRenderer());
    }
}
