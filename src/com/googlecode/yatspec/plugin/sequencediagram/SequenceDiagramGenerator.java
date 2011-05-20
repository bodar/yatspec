package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SequenceDiagramGenerator {
    private final static String FROM = "from";
    private final static String TO = "to";
    private final static String DEFAULT_SUBJECT = "Under Test";

    private final String subject;

    private CapturedInputAndOutputs capturedInputAndOutputs;
    private String FULLY_QUALIFIED_MESSAGE_SEND_REGEXP = "(.*) " + FROM + " (.*) " + TO + " (.*)";
    private String MESSAGE_SEND_WITH_DEFAULT_RECEIVER_REGEXP = "(.*) " + FROM + " (.*)";
    private String MESSAGE_SEND_WITH_DEFAULT_SENDER_REGEXP = "(.*) " + TO + " (.*)";
    private StringBuffer plantUmlCollector;

    public SequenceDiagramGenerator(CapturedInputAndOutputs capturedInputAndOutputs) {
        this(capturedInputAndOutputs, DEFAULT_SUBJECT);
    }

    public SequenceDiagramGenerator(CapturedInputAndOutputs capturedInputAndOutputs, String subject) {
        this.capturedInputAndOutputs = capturedInputAndOutputs;
        this.subject = subject;
    }

    public void logPlantUmlMarkupTo(StringBuffer plantUmlCollector) {
        this.plantUmlCollector = plantUmlCollector;
    }

    public void generateSequenceDiagram() {
        List<SequenceDiagramMessage> messagesCollector = new ArrayList<SequenceDiagramMessage>();
        List<String> actorNamesCollector = new ArrayList<String>();
        StringBuffer plantUmlMarkup = new StringBuffer("@startuml\n");
        for (Map.Entry<String, Object> captured : capturedInputAndOutputs.getTypes().entrySet()) {
            final String name = captured.getKey();
            if (name.matches(FULLY_QUALIFIED_MESSAGE_SEND_REGEXP)) {
                plantUmlMarkup.append(fromToStatement(name, messagesCollector, actorNamesCollector) + "\n");
            } else {
                if (name.matches(MESSAGE_SEND_WITH_DEFAULT_RECEIVER_REGEXP)) {
                    plantUmlMarkup.append(fromStatement(name, messagesCollector, actorNamesCollector) + "\n");
                }
                if (name.matches(MESSAGE_SEND_WITH_DEFAULT_SENDER_REGEXP)) {
                    plantUmlMarkup.append(toStatement(name, messagesCollector, actorNamesCollector) + "\n");
                }
            }
        }
        plantUmlMarkup.append("@enduml\n");
        if (plantUmlCollector != null) {
            plantUmlCollector.append(plantUmlMarkup);
        }

        SourceStringReader reader = new SourceStringReader(plantUmlMarkup.toString());
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String svg = new String(os.toByteArray());
        svg = svg.replaceFirst("<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>", "");
        svg = svg.replaceFirst("position:absolute;top:0;left:0;", "");
        svg = new SequenceDiagramHyperlinker().hyperlinkSequenceDiagram(actorNamesCollector, messagesCollector, svg);
        capturedInputAndOutputs.add("Sequence diagram", new SvgWrapper(svg));
    }

    private String fromToStatement(String capturedInputAndOutputName, List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
        final Pattern pattern = Pattern.compile(FULLY_QUALIFIED_MESSAGE_SEND_REGEXP);
        final java.util.regex.Matcher matcher = pattern.matcher(capturedInputAndOutputName);
        matcher.matches();
        final String what = matcher.group(1).trim();
        final String from = matcher.group(2).trim();
        final String to = matcher.group(3).trim();
        messagesCollector.add(new SequenceDiagramMessage(what, capturedInputAndOutputName.trim()));
        addActorName(actorNamesCollector, from);
        addActorName(actorNamesCollector, to);
        return from + " ->> " + to + ":" + what;
    }

    private void addActorName(List<String> actorNamesCollector, String name) {
        if (!actorNamesCollector.contains(name)) {
            actorNamesCollector.add(name.trim());
        }
    }

    private String fromStatement(String capturedInputAndOutputName, List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
        final Pattern pattern = Pattern.compile(MESSAGE_SEND_WITH_DEFAULT_RECEIVER_REGEXP);
        final java.util.regex.Matcher matcher = pattern.matcher(capturedInputAndOutputName);
        matcher.matches();
        final String what = matcher.group(1).trim();
        final String who = matcher.group(2).trim();
        messagesCollector.add(new SequenceDiagramMessage(what, capturedInputAndOutputName.trim()));
        addActorName(actorNamesCollector, who);
        return who + " ->> " + subject + ":" + what;
    }

    private String toStatement(String capturedInputAndOutputName, List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
        final Pattern pattern = Pattern.compile("(.*) " + TO + " (.*)");
        final java.util.regex.Matcher matcher = pattern.matcher(capturedInputAndOutputName);
        matcher.matches();
        final String what = matcher.group(1).trim();
        final String who = matcher.group(2).trim();
        messagesCollector.add(new SequenceDiagramMessage(what, capturedInputAndOutputName.trim()));
        addActorName(actorNamesCollector, who);
        return subject + " ->> " + who + ":" + what;
    }

    public static Content getHeaderContentForModalWindows() {
        return new Content(SequenceDiagramGenerator.class.getResource("dialogScriptHeaderContent.html"));
    }
}