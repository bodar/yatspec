package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ByNamingConventionMessageProducerTest {

    @Test
    public void ignoresValuesWithoutNamingConvention() {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add("Shopping Basket", new Object());
        Sequence<SequenceDiagramMessage> messages = sequence(new ByNamingConventionMessageProducer().messages(inputAndOutputs));
        assertThat(messages.isEmpty(), is(true));
    }

    @Test
    public void convertsValuesWithNamingConventionToSequenceDiagramMessages() {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add("Kiss from Boy to Girl", new Object()).add("Slap from Girl to Boy", new Object());
        Sequence<SequenceDiagramMessage> messages = sequence(new ByNamingConventionMessageProducer().messages(inputAndOutputs));
        assertThat(messages.size(), is(equalTo(2)));
        assertThat(messages.first(), is(equalTo(new SequenceDiagramMessage("Boy", "Girl", "Kiss", "Kiss_from_Boy_to_Girl"))));
        assertThat(messages.second(), is(equalTo(new SequenceDiagramMessage("Girl", "Boy", "Slap", "Slap_from_Girl_to_Boy"))));
    }

    @Test
    public void dealsWithGroups() {
        CapturedInputAndOutputs inputAndOutputs = new CapturedInputAndOutputs().add("(grouped) Kiss from Boy to Girl", new Object());
        Sequence<SequenceDiagramMessage> messages = sequence(new ByNamingConventionMessageProducer().messages(inputAndOutputs));
        assertThat(messages.size(), is(equalTo(1)));
        assertThat(messages.first(), is(equalTo(new SequenceDiagramMessage("Boy", "Girl", "(grouped) Kiss", "_grouped__Kiss_from_Boy_to_Girl"))));
    }
}
