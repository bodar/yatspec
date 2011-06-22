package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.totallylazy.Pair;
import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.Pair.pair;
import static net.sourceforge.plantuml.FileFormat.SVG;

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
    private SequenceDiagramHyperlinker sequenceDiagramHyperlinker = new SequenceDiagramHyperlinker();

    private StringBuffer optionalPlantUmlCollector;

    public SequenceDiagramGenerator(CapturedInputAndOutputs capturedInputAndOutputs) {
        this(capturedInputAndOutputs, DEFAULT_SUBJECT);
    }

    public SequenceDiagramGenerator(CapturedInputAndOutputs capturedInputAndOutputs, String subject) {
        this.capturedInputAndOutputs = capturedInputAndOutputs;
        this.subject = subject;
    }

    public void logPlantUmlMarkupTo(StringBuffer plantUmlCollector) {
        this.optionalPlantUmlCollector = plantUmlCollector;
    }

    public void generateSequenceDiagram() {
        List<SequenceDiagramMessage> messagesCollector = new ArrayList<SequenceDiagramMessage>();
        List<String> actorNamesCollector = new ArrayList<String>();
        String plantUmlMarkup = collectPlantUmlMarkup(messagesCollector, actorNamesCollector);
        makePlantUmlAvailableToAnyRegisteredCollector(plantUmlMarkup);

        String svg = svgMarkupMunger.munge(createSvg(plantUmlMarkup));
        svg = sequenceDiagramHyperlinker.hyperlinkSequenceDiagram(actorNamesCollector, messagesCollector, svg);
        capturedInputAndOutputs.add("Sequence diagram", new SvgWrapper(svg));
    }

    private void makePlantUmlAvailableToAnyRegisteredCollector(String plantUmlMarkup) {
        if (optionalPlantUmlCollector != null) {
            optionalPlantUmlCollector.append(plantUmlMarkup);
        }
    }


    private String createSvg(String plantUmlMarkup) {
        SourceStringReader reader = new SourceStringReader(plantUmlMarkup);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            reader.generateImage(os, new FileFormatOption(SVG));
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new String(os.toByteArray());
    }

    private String collectPlantUmlMarkup(List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
        StringBuffer plantUmlMarkup = new StringBuffer("@startuml\n");
        for (Map.Entry<String, Object> captured : capturedInputAndOutputs.getTypes().entrySet()) {
            final String capturedInputName = captured.getKey();
            if (capturedInputName.matches(FULLY_QUALIFIED_MESSAGE_SEND_REGEXP)) {
                plantUmlMarkup.append(plantUmlMarkupForFullyQualifiedCapturedInput(capturedInputName, messagesCollector, actorNamesCollector) + "\n");
            } else {
                if (capturedInputName.matches(MESSAGE_SEND_WITH_DEFAULT_RECEIVER_REGEXP)) {
                    plantUmlMarkup.append(plantUmlForCapturedInputWithFromOnly(capturedInputName, messagesCollector, actorNamesCollector) + "\n");
                }
                if (capturedInputName.matches(MESSAGE_SEND_WITH_DEFAULT_SENDER_REGEXP)) {
                    plantUmlMarkup.append(plantUmlForCapturedInputWithToOnly(capturedInputName, messagesCollector, actorNamesCollector) + "\n");
                }
            }
        }
        return plantUmlMarkup.append("@enduml\n").toString();
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

    private void addActorName(List<String> actorNamesCollector, String name) {
        if (!actorNamesCollector.contains(name)) {
            actorNamesCollector.add(name.trim());
        }
    }

    private String plantUmlForCapturedInputWithFromOnly(String capturedInputName, List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
        final Pair<String, String> leftAndRight = fromTo(capturedInputName, messagesCollector, actorNamesCollector, Pattern.compile(MESSAGE_SEND_WITH_DEFAULT_RECEIVER_REGEXP));
        return leftAndRight.second() + " ->> " + subject + ":" + leftAndRight.first();
    }

    private String plantUmlForCapturedInputWithToOnly(String capturedInputName, List<SequenceDiagramMessage> messagesCollector, List<String> actorNamesCollector) {
        final Pair<String, String> leftAndRight = fromTo(capturedInputName, messagesCollector, actorNamesCollector, Pattern.compile(MESSAGE_SEND_WITH_DEFAULT_SENDER_REGEXP));
        return subject + " ->> " + leftAndRight.second() + ":" + leftAndRight;
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

    public static Content getHeaderContentForModalWindows() {
        return new Content(SequenceDiagramGenerator.class.getResource("dialogScriptHeaderContent.html"));
    }
}