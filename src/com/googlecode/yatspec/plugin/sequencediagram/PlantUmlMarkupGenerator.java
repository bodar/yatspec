package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.totallylazy.Pair;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.Pair.pair;
import static java.lang.String.format;

public class PlantUmlMarkupGenerator {
    public final static String DEFAULT_SUBJECT = "Under Test";

    private final static String FROM = "from";
    private final static String TO = "to";
    private final static String FULLY_QUALIFIED_MESSAGE_SEND_REGEXP = "(.*) " + FROM + " (.*) " + TO + " (.*)";
    private final static String MESSAGE_SEND_WITH_DEFAULT_RECEIVER_REGEXP = "(.*) " + FROM + " (.*)";
    private final static String MESSAGE_SEND_WITH_DEFAULT_SENDER_REGEXP = "(.*) " + TO + " (.*)";

    private final String subject;
    private final CapturedInputAndOutputs capturedInputAndOutputs;

    public PlantUmlMarkupGenerator(CapturedInputAndOutputs capturedInputAndOutputs, String subject) {
        this.capturedInputAndOutputs = capturedInputAndOutputs;
        this.subject = subject;
    }

    public String collectPlantUmlMarkup(List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
        Markup markup = new Markup();
        for (Map.Entry<String, Object> captured : capturedInputAndOutputs.getTypes().entrySet()) {
            final String capturedInputName = captured.getKey();
            if (capturedInputName.matches(FULLY_QUALIFIED_MESSAGE_SEND_REGEXP)) {
                markup.addMessage(plantUmlMarkupForFullyQualifiedCapturedInput(capturedInputName, messagesCollector, actorNamesCollector));
            } else {
                if (capturedInputName.matches(MESSAGE_SEND_WITH_DEFAULT_RECEIVER_REGEXP)) {
                    markup.addMessage(plantUmlForCapturedInputWithFromOnly(capturedInputName, messagesCollector, actorNamesCollector));
                }
                if (capturedInputName.matches(MESSAGE_SEND_WITH_DEFAULT_SENDER_REGEXP)) {
                    markup.addMessage(plantUmlForCapturedInputWithToOnly(capturedInputName, messagesCollector, actorNamesCollector));
                }
            }
        }
        return markup.build();
    }

    private String plantUmlMarkupForFullyQualifiedCapturedInput(String capturedInputName, List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
        final Pattern pattern = Pattern.compile(FULLY_QUALIFIED_MESSAGE_SEND_REGEXP);
        final java.util.regex.Matcher matcher = pattern.matcher(capturedInputName);
        matcher.matches();
        final String what = matcher.group(1).trim();
        final String from = matcher.group(2).trim();
        final String to = matcher.group(3).trim();
        messagesCollector.add(new SequenceDiagramMessage(what, capturedInputName.trim()));
        addActorName(actorNamesCollector, from);
        addActorName(actorNamesCollector, to);
        return from + " ->> " + to + ":" + what;
    }

    private String plantUmlForCapturedInputWithFromOnly(String capturedInputName, List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
        final Pair<String, String> leftAndRight = fromTo(capturedInputName, messagesCollector, actorNamesCollector, Pattern.compile(MESSAGE_SEND_WITH_DEFAULT_RECEIVER_REGEXP));
        return leftAndRight.second() + " ->> " + subject + ":" + leftAndRight.first();
    }

    private String plantUmlForCapturedInputWithToOnly(String capturedInputName, List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
        final Pair<String, String> leftAndRight = fromTo(capturedInputName, messagesCollector, actorNamesCollector, Pattern.compile(MESSAGE_SEND_WITH_DEFAULT_SENDER_REGEXP));
        return subject + " ->> " + leftAndRight.second() + ":" + leftAndRight.first();
    }

    private Pair<String,String> fromTo(String capturedInputName, List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector, Pattern pattern) {
        final java.util.regex.Matcher matcher = pattern.matcher(capturedInputName);
        matcher.matches();
        final String left = matcher.group(1).trim();
        final String right = matcher.group(2).trim();
        messagesCollector.add(new SequenceDiagramMessage(left, capturedInputName.trim()));
        addActorName(actorNamesCollector, right);
        return pair(left, right);
    }

    private void addActorName(List<String> actorNamesCollector, String name) {
        if (!actorNamesCollector.contains(name)) {
            actorNamesCollector.add(name.trim());
        }
    }

    private class Markup {
        private GroupHelper groupHelper = new GroupHelper();
        StringBuffer plantUmlMarkup = new StringBuffer(format("@startuml%n"));

        private void addMessage(String messageLine) {
            plantUmlMarkup.append(groupHelper.markupGroup(messageLine));
            plantUmlMarkup.append(format("%s%n", messageLine));
        }

        public String build() {
            plantUmlMarkup.append(groupHelper.cleanUpOpenGroups());
            plantUmlMarkup.append(format("@enduml%n"));
            return plantUmlMarkup.toString();
        }
    }
}