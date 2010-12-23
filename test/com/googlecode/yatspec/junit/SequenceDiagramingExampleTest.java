package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.ToStringRenderer;
import com.googlecode.yatspec.rendering.WithCustomHeaderContent;
import com.googlecode.yatspec.rendering.WithCustomRendering;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.InterestingGivens;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
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
public class SequenceDiagramingExampleTest extends TestState implements WithCustomRendering, WithCustomHeaderContent {
    private static final Object ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST = new Object();

    private SequenceDiagramGenerator sequenceDiagramGenerator;

    @Before
    public void setup() {
        sequenceDiagramGenerator = new SequenceDiagramGenerator(capturedInputAndOutputs, "bambam");
    }

    @Test
    public void bambamGetsFoodForHisDad() throws Exception {
        given(aHungryMrFlintstone());
        when(heDemandsFoodFromBambam());
        then(bambam(), placesABurgerOrderWithBarney());

        when(barney(), givesTheBurgerToBambam());
        then(bambam(), givesFoodToMrFlintstone());
    }

    public Content getCustomHeaderContent() {
        return SequenceDiagramGenerator.getHeaderContentForModalWindows();
    }

    public Map<Class, Renderer> getCustomRenderers() {
        return new HashMap<Class, Renderer>() {{
            put(SvgWrapper.class, new ToStringRenderer());
        }};
    }

    @After
    public void generateSequenceDiagram() {
        sequenceDiagramGenerator.generateSequenceDiagram();
    }

    private Matcher<? super Object> givesFoodToMrFlintstone() {
        capturedInputAndOutputs.add("food to mrflintstone", "here is your burger");
        return dummyMatcher();
    }

    private Object givesTheBurgerToBambam() {
        capturedInputAndOutputs.add("burger from barney", "1 burger here u go");
        return ANY_THING_FOR_THE_PURPOSES_OF_THIS_TEST;
    }

    private Matcher<? super Object> placesABurgerOrderWithBarney() {
        capturedInputAndOutputs.add("burger order to barney", "Get me a burger");
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
