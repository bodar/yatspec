package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.*;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.WithCustomHtmlHeaderContent;
import com.googlecode.yatspec.rendering.html.WithCustomHtmlRendering;
import com.googlecode.yatspec.state.givenwhenthen.*;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpecRunner.class)
public class SequenceDiagramingExampleTest extends TestState implements WithCustomHtmlRendering, WithCustomHtmlHeaderContent {
    private static final Object ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST = new Object();

    private SequenceDiagramGenerator sequenceDiagramGenerator;

    @Before
    public void setup() {
        sequenceDiagramGenerator = new SequenceDiagramGenerator(capturedInputAndOutputs, "bambam");
    }

    @Test
    public void bambamGetsFoodForHisDad() throws Exception {
        String testNumber = "test:1";
        given(aHungryMrFlintstone());
        when(barney(), givesTheBurgerToBambam(testNumber));
        then(bambam(), givesFoodToMrFlintstone(testNumber));

        when(heDemandsFoodFromBambam());
        then(bambam(), placesABurgerOrderWithBarney(testNumber));


        then(mrFlintstone(), sharesHisFoodWithBarneyBecauseHeLikesHim(testNumber));
    }

    @Test
    public void bambamGetsFoodForHisDadRepeatedSoWeCanCheckMultipleSequenceDiagramsOnOnePage() throws Exception {
        String testNumber = "test:2";
        given(aHungryMrFlintstone());
        when(heDemandsFoodFromBambam());
        then(bambam(), placesABurgerOrderWithBarney(testNumber));

        when(barney(), givesTheBurgerToBambam(testNumber));
        then(bambam(), givesFoodToMrFlintstone(testNumber));

        then(mrFlintstone(), sharesHisFoodWithBarneyBecauseHeLikesHim(testNumber));
    }


    @Table({
            @Row({"row_a"}), 
            @Row({"row_b"})
    })
    @Test
    public void bambamGetsFoodForHisDadRepeatedSoWeCanCheckMultipleScenariosPerTestMethod(String scenarioName) throws Exception {
        String testName = "test:3 scenario: "+scenarioName;
        given(aHungryMrFlintstone());
        when(heDemandsFoodFromBambam());
        then(bambam(), placesABurgerOrderWithBarney(testName));

        when(barney(), givesTheBurgerToBambam(testName));
        then(bambam(), givesFoodToMrFlintstone(testName));

        then(mrFlintstone(), sharesHisFoodWithBarneyBecauseHeLikesHim(testName));
    }

    public Content getCustomHeaderContent() {
        return SequenceDiagramGenerator.getHeaderContentForModalWindows();
    }

    public Map<Class, Renderer> getCustomHtmlRenderers() {
        return new HashMap<Class, Renderer>() {{
            put(SvgWrapper.class, new DontHighlightRenderer());
        }};
    }

    @After
    public void generateSequenceDiagram() {
        sequenceDiagramGenerator.generateSequenceDiagram();
    }

    private Matcher<? super Object> sharesHisFoodWithBarneyBecauseHeLikesHim(String testNumber) {
        capturedInputAndOutputs.add("(grouped) food from mrflintstone to barney", "have some of my burger because I like you (test:" + testNumber + ")");
        return dummyMatcher();
    }

    private Matcher<? super Object> givesFoodToMrFlintstone(String testNumber) {
        capturedInputAndOutputs.add("(grouped) food to mrflintstone", "here is your burger (test:" + testNumber +")");
        return dummyMatcher();
    }

    private Object givesTheBurgerToBambam(String testNumber) {
        capturedInputAndOutputs.add("burger from barney", "1 burger here u go (test:" + testNumber +")");
        return ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST;
    }

    private Matcher<? super Object> placesABurgerOrderWithBarney(String testNumber) {
        capturedInputAndOutputs.add("burger order to barney", "Get me a burger (test:" + testNumber +")");
        interestingGivens.add("burger");
        return dummyMatcher();
    }

    private ActionUnderTest heDemandsFoodFromBambam() {
        return new ActionUnderTest() {
            public CapturedInputAndOutputs execute(InterestingGivens givens, CapturedInputAndOutputs capturedInputAndOutputs) {
                capturedInputAndOutputs.add("food demand from mrflintstone", "I want a burger");
                return capturedInputAndOutputs;
            }
        };
    }


    private StateExtractor<Object> bambam() {
        return new StateExtractor<Object>() {
            public Object execute(CapturedInputAndOutputs inputAndOutputs) throws Exception {
                return ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST;
            }
        };
    }

    private StateExtractor<Object> mrFlintstone() {
        return new StateExtractor<Object>() {
            public Object execute(CapturedInputAndOutputs inputAndOutputs) throws Exception {
                return ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST;
            }
        };
    }

    private StateExtractor<Object> barney() {
        return new StateExtractor<Object>() {
            public Object execute(CapturedInputAndOutputs inputAndOutputs) throws Exception {
                return ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST;
            }
        };
    }

    private GivensBuilder aHungryMrFlintstone() {
        return new GivensBuilder() {
            public InterestingGivens build(InterestingGivens givens) throws Exception {
                return givens;
            }
        };
    }

    public void when(Object o, Object oo) {}

    private BaseMatcher<Object> dummyMatcher() {
        return new BaseMatcher<Object>() {
            public boolean matches(Object o) { return true; }
            public void describeTo(Description description) {}
        };
    }

}
