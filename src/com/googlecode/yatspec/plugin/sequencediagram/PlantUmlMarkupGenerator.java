package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;

public class PlantUmlMarkupGenerator {
    public String generateMarkup(Iterable<SequenceDiagramMessage> messages) {
        Markup markup = new Markup();
        sequence(messages).map(plantUmlMarkup()).forEach(addTo(markup));
        return markup.build();
    }

    private Callable1<String, Void> addTo(final Markup markup) {
        return new Callable1<String, Void>() {
            @Override
            public Void call(String s) throws Exception {
                markup.addMessage(s);
                return null;
            }
        };
    }

    private Callable1<SequenceDiagramMessage, String> plantUmlMarkup() {
        return new Callable1<SequenceDiagramMessage, String>() {
            @Override
            public String call(SequenceDiagramMessage message) throws Exception {
                return String.format("%s ->> %s:<text class=sequence_diagram_clickable sequence_diagram_message_id=%s>%s</text>", message.from(), message.to(), message.messageId(), message.messageName());
            }
        };
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