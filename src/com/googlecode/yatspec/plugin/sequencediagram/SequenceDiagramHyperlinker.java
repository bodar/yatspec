package com.googlecode.yatspec.plugin.sequencediagram;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class SequenceDiagramHyperlinker {
    public String hyperlinkSequenceDiagram(List<String> actorNames, List<SequenceDiagramMessage> messages, String sequenceDiagramAsSvg) {
        List<String> remainingInputLines = new ArrayList<String>(Arrays.asList(prettyPrint(sequenceDiagramAsSvg).split("\n")));
        dropTheXmlHeaderAndTrailingEmptyLine(remainingInputLines);
        List<String> outputLines = new ArrayList<String>();
        for (String actorName : actorNames) {
            addHyperlinkForActor(actorName, outputLines, remainingInputLines);
        }
        for (SequenceDiagramMessage currentMessage : messages) {
            addHyperlinkForMessage(currentMessage, outputLines, remainingInputLines);
        }
        outputLines.addAll(remainingInputLines);
        return join(outputLines, "\n").trim();
    }

    private void addHyperlinkForActor(String actorName, List<String> outputLines, List<String> remainingInputLines) {
        while (!remainingInputLines.isEmpty()) {
            String currentInputLine = remainingInputLines.get(0).trim();
            remainingInputLines.remove(0);
            if (isActorLine(actorName, currentInputLine)) {
                outputLines.add(withActorHyperLink(currentInputLine, actorName));
                break;
            } else {
                outputLines.add(currentInputLine);
            }
        }
    }

    private void addHyperlinkForMessage(SequenceDiagramMessage currentMessage, List<String> outputLines, List<String> remainingInputLines) {
        while (!remainingInputLines.isEmpty()) {
            String currentInputLine = remainingInputLines.get(0).trim();
            remainingInputLines.remove(0);
            if (isSequenceDiagramLineForMessage(currentMessage, currentInputLine)) {
                outputLines.add(withHyperLink(currentInputLine, currentMessage));
                break;
            } else {
                outputLines.add(currentInputLine);
            }
        }
    }

    private void dropTheXmlHeaderAndTrailingEmptyLine(List<String> remainingInputLines) {
        remainingInputLines.remove(0);
        remainingInputLines.remove(remainingInputLines.size() - 1);
    }

    private static boolean isActorLine(String actorName, String currentInputLine) {
        return currentInputLine.startsWith("<text") && currentInputLine.matches(".*>" + actorName + "<.*");
    }

    private static boolean isSequenceDiagramLineForMessage(SequenceDiagramMessage currentMessage, String currentInputLine) {
        return currentInputLine.startsWith("<text") && currentInputLine.matches(".*>" + currentMessage.getVisibleName() + "<.*");
    }

    private String join(List<String> lines, String delim) {
        StringBuffer result = new StringBuffer();
        for (String line : lines) {
            result.append(line.trim()).append(delim);
        }
        return result.toString();
    }

    private String withActorHyperLink(String currentInputLine, String actorName) {
        final String regexp = ".*(<text .*?>" + actorName + "</text>).*";
        final Pattern pattern = Pattern.compile(regexp);
        final java.util.regex.Matcher matcher = pattern.matcher(currentInputLine);
        matcher.matches();
        return currentInputLine.replaceFirst("(.*)(<text .*?>" + actorName + "</text>)(.*)", "$1<a class=\"sequence_diagram_actor\" sequence_diagram_actor_id=\"" + actorName + "\" href=\"#\">$2</a>$3");
    }

    private String withHyperLink(String currentInputLine, SequenceDiagramMessage currentMessage) {
        final String regexp = ".*(<text .*?>" + currentMessage.getVisibleName() + "</text>).*";
        final Pattern pattern = Pattern.compile(regexp);
        final java.util.regex.Matcher matcher = pattern.matcher(currentInputLine);
        matcher.matches();
        return currentInputLine.replaceFirst("(.*)(<text .*?>" + currentMessage.getVisibleName() + "</text>)(.*)", "$1<a class=\"sequence_diagram_clickable\" sequence_diagram_message_id=\"" + currentMessage.getFullyQualifiedMessageName().replaceAll(" ", "_") + "\" href=\"#\">$2</a>$3");
    }

    private String prettyPrint(String sequenceDiagramAsSvg) {
        try {
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(new StringReader(sequenceDiagramAsSvg));
            StringWriter stringWriter = new StringWriter();
            new XMLOutputter(Format.getPrettyFormat()).output(doc, stringWriter);
            return stringWriter.toString();
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
