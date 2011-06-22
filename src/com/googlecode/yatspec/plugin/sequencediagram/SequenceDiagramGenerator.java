package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.totallylazy.Pair;
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
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.Pair.pair;

public class SequenceDiagramGenerator {
    private final static String FROM = "from";
    private final static String TO = "to";
    private final static String DEFAULT_SUBJECT = "Under Test";
    private final static String FULLY_QUALIFIED_MESSAGE_SEND_REGEXP = "(.*) " + FROM + " (.*) " + TO + " (.*)";
    private final static String MESSAGE_SEND_WITH_DEFAULT_RECEIVER_REGEXP = "(.*) " + FROM + " (.*)";
    private final static String MESSAGE_SEND_WITH_DEFAULT_SENDER_REGEXP = "(.*) " + TO + " (.*)";

    private final String subject;
    private final CapturedInputAndOutputs capturedInputAndOutputs;
    private final PlantUmlSvgMarkupMunger svgMarkupMunger = new PlantUmlSvgMarkupMunger();
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
        StringBuffer plantUmlMarkup = createPlantUmlMarkup(messagesCollector, actorNamesCollector);
        makePlantUmlAvailable(plantUmlMarkup);

        String svg = svgMarkupMunger.munge(createSvg(plantUmlMarkup));
        svg = new SequenceDiagramHyperlinker().hyperlinkSequenceDiagram(actorNamesCollector, messagesCollector, svg);
        capturedInputAndOutputs.add("Sequence diagram", new SvgWrapper(svg));
    }

    private void makePlantUmlAvailable(StringBuffer plantUmlMarkup) {
        if (plantUmlCollector != null) {
            plantUmlCollector.append(plantUmlMarkup);
        }
    }


    private String createSvg(StringBuffer plantUmlMarkup) {
        SourceStringReader reader = new SourceStringReader(plantUmlMarkup.toString());
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new String(os.toByteArray());
    }

    private StringBuffer createPlantUmlMarkup(List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
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
        return plantUmlMarkup;
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
        final Pair<String, String> leftAndRight = fromTo(capturedInputAndOutputName, messagesCollector, actorNamesCollector, Pattern.compile(MESSAGE_SEND_WITH_DEFAULT_RECEIVER_REGEXP));
        return leftAndRight.second() + " ->> " + subject + ":" + leftAndRight.first();
    }

    private String toStatement(String capturedInputAndOutputName, List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
        final Pair<String, String> leftAndRight = fromTo(capturedInputAndOutputName, messagesCollector, actorNamesCollector, Pattern.compile(MESSAGE_SEND_WITH_DEFAULT_SENDER_REGEXP));
        return subject + " ->> " + leftAndRight.second() + ":" + leftAndRight;
    }
    
    private Pair<String,String> fromTo(String capturedInputAndOutputName, List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector, Pattern pattern) {
        final java.util.regex.Matcher matcher = pattern.matcher(capturedInputAndOutputName);
        matcher.matches();
        final String left = matcher.group(1).trim();
        final String right = matcher.group(2).trim();
        messagesCollector.add(new SequenceDiagramMessage(left, capturedInputAndOutputName.trim()));
        addActorName(actorNamesCollector, right);
        return pair(left, right);
    }

    public static Content getHeaderContentForModalWindows() {
        return new Content(SequenceDiagramGenerator.class.getResource("dialogScriptHeaderContent.html"));
    }
}