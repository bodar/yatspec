package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByNamingConventionMessageProducer {
    private final static String FULLY_QUALIFIED_MESSAGE_SEND_REGEXP = "(.*) from (.*) to (.*)";

    private final Pattern pattern;

    public ByNamingConventionMessageProducer() {
        pattern = Pattern.compile(FULLY_QUALIFIED_MESSAGE_SEND_REGEXP);
    }

    public Iterable<SequenceDiagramMessage> messages(CapturedInputAndOutputs inputAndOutputs) {
        Sequence<SequenceDiagramMessage> result = Sequences.empty();
        Set<String> keys = inputAndOutputs.getTypes().keySet();
        for (String key : keys) {
            Matcher matcher = pattern.matcher(key);
            if (matcher.matches()) {
                final String what = matcher.group(1).trim();
                final String from = matcher.group(2).trim();
                final String to = matcher.group(3).trim();
                result = result.append(new SequenceDiagramMessage(from, to, what, key.replaceAll(" ","_").replaceAll("\\(","_").replaceAll("\\)","_")));
            }
        }
        return result;
    }
}
