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
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

public class SequenceDiagramHyperlinker {

    public String hyperlinkSequenceDiagram(List<String> actorNames, List<SequenceDiagramMessage> messages, String sequenceDiagramAsSvg) {
        List<String> inputLines = dropTheXmlHeaderAndTrailingEmptyLine(asList(prettyPrint(sequenceDiagramAsSvg).split("\n")));
        List<String> outputLines = new ArrayList<String>();

        ActorLineDecorator actorLineDecorator = new ActorLineDecorator(actorNames);
        MessageLineDecorator messageLineDecorator = new MessageLineDecorator(messages);

        for (String line : inputLines) {
            String currentLine = line.trim();

            currentLine = actorLineDecorator.decorate(currentLine);
            currentLine = messageLineDecorator.decorate(currentLine);

            outputLines.add(currentLine);
        }

        return join(outputLines, "\n");
    }

    private List<String> dropTheXmlHeaderAndTrailingEmptyLine(List<String> inputLines) {
        List<String> outputLinesWithoutHeaders = new ArrayList<String>(inputLines);

        outputLinesWithoutHeaders.remove(0);
        outputLinesWithoutHeaders.remove(outputLinesWithoutHeaders.size() - 1);

        return outputLinesWithoutHeaders;
    }


    private String join(List<String> lines, String delim) {
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            result.append(line.trim()).append(delim);
        }

        return result.toString().trim();
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


    private static class ActorLineDecorator {
        private final List<String> actors;

        public ActorLineDecorator(List<String> actors) {
            this.actors = new ArrayList<String>(actors);
        }

        public String decorate(String line) {
            String actorNameFound = findActor(line);

            if (actorNameFound != null) {
                actors.remove(actorNameFound);
                return withActorHyperLink(line, actorNameFound);
            }

            return line;
        }

        private String findActor(String line) {
            for (String actorName : actors) {
                if (isActorLine(actorName, line))
                    return actorName;
            }
            return null;
        }

        private String withActorHyperLink(String currentInputLine, String actorName) {
            return currentInputLine.replaceFirst("(.*)(<text .*?>" + actorName + "</text>)(.*)", "$1<a class=\"sequence_diagram_actor\" sequence_diagram_actor_id=\"" + actorName + "\" href=\"#\">$2</a>$3");
        }

        private static boolean isActorLine(String actorName, String currentInputLine) {
            return currentInputLine.startsWith("<text") && currentInputLine.matches(".*>" + actorName + "<.*");
        }
    }


    private static class MessageLineDecorator {
        private LinkedList<SequenceDiagramMessage> messages;

        public MessageLineDecorator(List<SequenceDiagramMessage> messages) {
            this.messages = new LinkedList<SequenceDiagramMessage>(messages);
        }

        public String decorate(String currentLine) {
            if (!messages.isEmpty()) {
                if (isSequenceDiagramLineForMessage(messages.peek(), currentLine)) {
                    return withHyperLink(currentLine, messages.poll());
                }
            }

            return currentLine;
        }

        private String withHyperLink(String currentInputLine, SequenceDiagramMessage currentMessage) {
            return currentInputLine.replaceFirst("(.*)(<text .*?>" + regexGroupsSafe(currentMessage.getVisibleName()) + "</text>)(.*)", "$1<a class=\"sequence_diagram_clickable\" sequence_diagram_message_id=\"" + currentMessage.getFullyQualifiedMessageName().replaceAll(" ", "_") + "\" href=\"#\">$2</a>$3");
        }

        private static boolean isSequenceDiagramLineForMessage(SequenceDiagramMessage currentMessage, String currentInputLine) {
            return currentInputLine.startsWith("<text") && currentInputLine.matches(".*>" + regexGroupsSafe(currentMessage.getVisibleName()) + "<.*");
        }

        private static String regexGroupsSafe(final String visibleName) {
            return visibleName.replace("(", "\\(").replace(")", "\\)");
        }
    }
}
