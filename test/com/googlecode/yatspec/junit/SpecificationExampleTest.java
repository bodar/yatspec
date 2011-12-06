package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.rendering.html.HyperlinkRenderer;
import com.googlecode.yatspec.rendering.NotesRenderer;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.WithCustomRendering;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static com.googlecode.yatspec.plugin.jdom.StateExtractors.getValue;
import static java.lang.Double.valueOf;
import static java.lang.Math.sqrt;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpecRunner.class)
@Notes("This is a note on the whole class\n" +
        "It will preserve space")
public class SpecificationExampleTest extends TestState implements WithCustomRendering {
    private static final String RADICAND = "Radicand";
    private static final String RESULT = "Result";

    @Test
    public void reallySimpleExample() throws Exception {
        assertThat(sqrt(9), is(3.0));
    }

    @Test
    @Table({@Row({"9", "3.0"}),
            @Row({"16", "4.0"})})
    @Notes("This example combines table / row tests with specification and given when then")
    public void takeTheSquareRoot(String radicand, String result) throws Exception {
        given(theRadicand(radicand));
        when(weTakeTheSquareRoot());
        then(theResult(), is(valueOf(result)));
    }

    @Test
    @Ignore
    public void printEmptyTestName() throws Exception {

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

    @SuppressWarnings({"unchecked"})
    @Override
    public Map<Class, Renderer> getCustomRenderers() {
        return new HashMap(singletonMap(Notes.class, new HyperlinkRenderer("http://localhost:8080", new NotesRenderer())));
    }
}
