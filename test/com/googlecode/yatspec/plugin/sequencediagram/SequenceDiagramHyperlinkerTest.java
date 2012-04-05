package com.googlecode.yatspec.plugin.sequencediagram;

import org.junit.Test;

import java.util.*;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class SequenceDiagramHyperlinkerTest {
    private final String inputSvg =
            "<svg xmlns=\"http://www.w3.org/2000/svg\" style=\"width:100%;height:100%;\" version=\"1.1\">\n" +
                    "    <defs/>\n" +
                    "    <g>\n" +
                    "        <line/>\n" +
                    "        <rect/>\n" +
                    "        <text font-size=\"9\">barney</text>\n" +
                    "        <rect/>\n" +
                    "        <text font-size=\"10\">fred</text>\n" +
                    "        <text font-size=\"11\">open</text>\n" +
                    "        <line/>\n" +
                    "        <text font-size=\"12\">close</text>\n" +
                    "        <line/>\n" +
                    "        <text font-size=\"13\">open</text>\n" +
                    "        <line/>\n" +
                    "        <text font-size=\"14\">fred</text>\n" +
                    "    </g>\n" +
                    "</svg>";

    private final String expectedSvg =
            "<svg xmlns=\"http://www.w3.org/2000/svg\" style=\"width:100%;height:100%;\" version=\"1.1\">\n" +
                    "<defs />\n" +
                    "<g>\n" +
                    "<line />\n" +
                    "<rect />\n" +
                    "<a class=\"sequence_diagram_actor\" sequence_diagram_actor_id=\"barney\" href=\"#\"><text font-size=\"9\">barney</text></a>\n" +
                    "<rect />\n" +
                    "<a class=\"sequence_diagram_actor\" sequence_diagram_actor_id=\"fred\" href=\"#\"><text font-size=\"10\">fred</text></a>\n" +
                    "<a class=\"sequence_diagram_clickable\" sequence_diagram_message_id=\"open_from_someone\" href=\"#\"><text font-size=\"11\">open</text></a>\n" +
                    "<line />\n" +
                    "<a class=\"sequence_diagram_clickable\" sequence_diagram_message_id=\"close_to_someone\" href=\"#\"><text font-size=\"12\">close</text></a>\n" +
                    "<line />\n" +
                    "<a class=\"sequence_diagram_clickable\" sequence_diagram_message_id=\"second_open_from_someone\" href=\"#\"><text font-size=\"13\">open</text></a>\n" +
                    "<line />\n" +
                    "<a class=\"sequence_diagram_clickable\" sequence_diagram_message_id=\"message_with_actor_name\" href=\"#\"><text font-size=\"14\">fred</text></a>\n" +
                    "</g>\n" +
                    "</svg>";

    @Test
    public void insertsHyperlinks() throws Exception {
        List<SequenceDiagramMessage> messages = asList(
                new SequenceDiagramMessage("open", "open from someone"),
                new SequenceDiagramMessage("close", "close to someone"),
                new SequenceDiagramMessage("open", "second open from someone"),
                new SequenceDiagramMessage("fred", "message with actor name")
        );
        List<String> actorNames = asList("fred", "barney");

        String outputSvg = new SequenceDiagramHyperlinker().hyperlinkSequenceDiagram(actorNames, messages, inputSvg);

        assertEquals(expectedSvg, outputSvg);
    }
}