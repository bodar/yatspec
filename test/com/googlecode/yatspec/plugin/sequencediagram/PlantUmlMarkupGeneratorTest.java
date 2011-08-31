package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PlantUmlMarkupGeneratorTest {
    
    @Test
    public void doesFromTo() throws Exception {
        CapturedInputAndOutputs capturedInputAndOutputs = new CapturedInputAndOutputs();
        capturedInputAndOutputs.add("a message from here to there", "message body");
        String markup = new PlantUmlMarkupGenerator(capturedInputAndOutputs, "don't care").collectPlantUmlMarkup(new ArrayList<SequenceDiagramMessage>(), new ArrayList<String>());
        assertThat(markup, is(markupContaining("here ->> there:a message")));
    }

    @Test
    public void doesGroups() throws Exception {
        CapturedInputAndOutputs capturedInputAndOutputs = new CapturedInputAndOutputs();
        capturedInputAndOutputs.add("(hello) a message from here to there", "message body");
        String markup = new PlantUmlMarkupGenerator(capturedInputAndOutputs, "don't care").collectPlantUmlMarkup(new ArrayList<SequenceDiagramMessage>(), new ArrayList<String>());
        assertThat(markup, is(markupContaining("group hello", "here ->> there:(hello) a message" ,"end")));
    }

    private Matcher<? super String> markupContaining(final String... expected) {
        StringBuilder expectation = new StringBuilder();
        expectation.append(String.format("@startuml%n"));
        for (String s : expected) {
            expectation.append(String.format("%s%n", s));
        }
        expectation.append(String.format("@enduml%n"));
        return is(expectation.toString());
    }
}
