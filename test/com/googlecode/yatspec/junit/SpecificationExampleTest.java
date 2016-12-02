package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.rendering.NotesRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.HyperlinkRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.rendering.html.tagindex.HtmlTagIndexRenderer;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.plugin.jdom.StateExtractors.getValue;
import static java.lang.Double.valueOf;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpecRunner.class)
@Notes("This is a note on the whole class\n" +
        "It will preserve space")
public class SpecificationExampleTest extends TestState implements WithCustomResultListeners {
    private static final String RADICAND = "Radicand";
    private static final String RESULT = "Result";
    private int aNumber;

    @Test
    @Notes("#tag-one")
    public void reallySimpleExample() throws Exception {
        assertThat(sqrt(9), is(3.0));
    }

    @Test
    @Table({@Row({"9", "3.0"}),
            @Row({"16", "4.0"})})
    @Notes("#tag-one\n" +
            "#tag-two\n" +
            "This example combines table / row tests with specification and given when then")
    public void takeTheSquareRoot(String radicand, String result) throws Exception {
        given(theRadicand(radicand));
        when(weTakeTheSquareRoot());
        then(theResult(), is(valueOf(result)));
    }

    @Test
    @LinkingNote(message = "The details of how the Linking Note works can be seen in the %s", links = {LinkingNoteRendererTest.class})
    public void testWithALinkingNote() throws Exception {

    }

    @Test
    public void printEmptyTestName() throws Exception {

    }

    @Test
    @Table({@Row({"someParam", "varargA", "varargB"}),
            @Row({"anotherParam"})})
    public void callMethodsWithTrailingVarargs(String firstParam, String... otherParams) throws Exception {
        assertThat(firstParam, Matchers.not(Matchers.isIn(otherParams)));
    }

    @Test
    public void collapsed() throws Exception {
//        mathFloorRoundsDown(1, "234234", "no collapse");
        mathFloorRoundsDown(1, "234234");
        mathFloorRoundsDown(1, 1);
        mathFloorRoundsDown("1");

        mathFloorRoundsDown(1);
        mathFloorRoundsDown();
        andMathCeilRoundsUp();
    }

    @Collapsible
    private void mathFloorRoundsDown(int i) {
        assertThat(ceil(1.02), is(2.0));
    }

    @Collapsible
    private void mathFloorRoundsDown(String s) {
        assertThat(ceil(1.01), is(2.0));
    }

    private void mathFloorRoundsDown(int i, String s, String s2) {
        assertThat(ceil(1.1), is(2.0));
    }

    @Collapsible
    private void mathFloorRoundsDown(int i, String s) {
        assertThat(ceil(1.1), is(2.0));
    }

    @Collapsible
    private void mathFloorRoundsDown(int i, int s) {
        assertThat(ceil(1.2), is(2.0));
    }

    @Collapsible
    private void mathFloorRoundsDown() {
        assertThat(floor(1.3), is(1.0));
    }

    @Collapsible
    private void andMathCeilRoundsUp() {
        assertThat(ceil(1.4), is(2.0));
    }

    private GivensBuilder theRadicand(final String number) {
        return theRadicand(Integer.valueOf(number));
    }

    private GivensBuilder theRadicand(final int number) {
        return new GivensBuilder() {
            public InterestingGivens build(InterestingGivens interestingGivens) throws Exception {
                return interestingGivens.add(RADICAND, number);
            }
        };
    }

    private ActionUnderTest weTakeTheSquareRoot() {
        return new ActionUnderTest() {
            public CapturedInputAndOutputs execute(InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) {
                int number = interestingGivens.getType(RADICAND, Integer.class);
                return capturedInputAndOutputs.add(RESULT, sqrt(number));
            }
        };
    }

    private static StateExtractor<Double> theResult() {
        return getValue(RESULT, Double.class);
    }

    public Iterable<SpecResultListener> getResultListeners() throws Exception {
        return sequence(
                new HtmlResultRenderer().
                        withCustomRenderer(Notes.class, new HyperlinkRenderer(new NotesRenderer(), "(?:#)([^\\s]+)", "<a href='http://localhost:8080/pretent-issue-tracking/$1'>$1</a>")),
                new HtmlIndexRenderer(),
                new HtmlTagIndexRenderer());
    }
}
