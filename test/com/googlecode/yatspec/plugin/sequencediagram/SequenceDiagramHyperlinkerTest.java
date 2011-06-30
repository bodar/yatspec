package com.googlecode.yatspec.plugin.sequencediagram;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                    "</g>\n" +
                    "</svg>";
    @Test
    public void insertsHyperlinksOnMessages() throws Exception {
        List<SequenceDiagramMessage> messages = new ArrayList<SequenceDiagramMessage>();
        messages.add(new SequenceDiagramMessage("open", "open from someone"));
        messages.add(new SequenceDiagramMessage("close", "close to someone"));
        messages.add(new SequenceDiagramMessage("open", "second open from someone"));
        List<String> actorNames = new ArrayList<String>();
        actorNames.add("barney");
        actorNames.add("fred");
        String outputSvg = new SequenceDiagramHyperlinker().hyperlinkSequenceDiagram(actorNames,messages, inputSvg);
        assertEquals(expectedSvg, outputSvg);
    }
}