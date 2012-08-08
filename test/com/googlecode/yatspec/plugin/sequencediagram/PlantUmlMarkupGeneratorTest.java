package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import org.hamcrest.Matcher;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PlantUmlMarkupGeneratorTest {

    @Test
    public void generatesPlantUmlMarkup() {
        String markup = new PlantUmlMarkupGenerator().generateMarkup(sequence(new SequenceDiagramMessage("Bob", "Alice", "How are you Alice?", "message_id")));
        assertThat(markup, is(markupContaining("Bob ->> Alice:<text class=sequence_diagram_clickable sequence_diagram_message_id=message_id>How are you Alice?</text>")));
    }

    @Test
    public void doesGroups() throws Exception {
        CapturedInputAndOutputs capturedInputAndOutputs = new CapturedInputAndOutputs();
        capturedInputAndOutputs.add("(hello) a message from here to there", "message body");
        String markup = new PlantUmlMarkupGenerator().generateMarkup(sequence(new SequenceDiagramMessage("here", "there", "(hello) a message", "message_id")));
        assertThat(markup, is(markupContaining("group hello", "here ->> there:<text class=sequence_diagram_clickable sequence_diagram_message_id=message_id>(hello) a message</text>", "end")));
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
