package com.googlecode.yatspec.plugin.sequencediagram;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SequenceDiagramHyperlinkerTest {
    private final String inputSvg = 
            "<svg xmlns=\"http://www.w3.org/2000/svg\" style=\"width:100%;height:100%;\" version=\"1.1\">\n" +
            "    <defs/>\n" +
            "    <g>\n" +
            "        <line/>\n" +
            "        <rect/>\n" +
            "        <text font-size=\"9\">mrflintstone</text>\n" +
            "        <rect/>\n" +
            "        <text font-size=\"10\">mrflintstone</text>\n" +
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
                    "<text font-size=\"9\">mrflintstone</text>\n" +
                    "<rect />\n" +
                    "<text font-size=\"10\">mrflintstone</text>\n" +
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
        String outputSvg = new SequenceDiagramHyperlinker().hyperlinkSequenceDiagram(messages, inputSvg);
        assertEquals(expectedSvg, outputSvg);
    }
}