package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SequenceDiagramGenerator {
    private final static String FROM = "from";
    private final static String TO = "to";
    private final static String DEFAULT_SUBJECT = "Under Test";

    private final String subject;

    private CapturedInputAndOutputs capturedInputAndOutputs;

    public SequenceDiagramGenerator(CapturedInputAndOutputs capturedInputAndOutputs) {
        this(capturedInputAndOutputs, DEFAULT_SUBJECT);
    }

    public SequenceDiagramGenerator(CapturedInputAndOutputs capturedInputAndOutputs, String subject) {
        this.capturedInputAndOutputs = capturedInputAndOutputs;
        this.subject = subject;
    }

    public void generateSequenceDiagram() {
        Map<String, String> arrowNamesCollector = new HashMap<String, String>();
        StringBuffer buffer = new StringBuffer("@startuml\n");
        for (Map.Entry<String, Object> captured : capturedInputAndOutputs.entrySet()) {
            final String name = captured.getKey();
            if (name.matches("(.*) " + FROM + " (.*)")) {
                buffer.append(printFromStatement(name, arrowNamesCollector) + "\n");
            }
            if (name.matches("(.*) " + TO + " (.*)")) {
                buffer.append(printToStatement(name, arrowNamesCollector) + "\n");
            }
        }
        buffer.append("@enduml\n");
        SourceStringReader reader = new SourceStringReader(buffer.toString());
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            reader.generateImage(os, FileFormat.SVG);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String svg = new String(os.toByteArray());
        svg = svg.replaceFirst("<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>", "");
        svg = svg.replaceFirst("position:absolute;top:0;left:0;", "");
        for (Map.Entry<String, String> arrowName : arrowNamesCollector.entrySet()) {

            final String regexp = ".*(<text .*?>" + arrowName.getKey() + "</text>).*";
            final Pattern pattern = Pattern.compile(regexp);
            final java.util.regex.Matcher matcher = pattern.matcher(svg);
            matcher.matches();
            svg = svg.replaceFirst("(.*)(<text .*?>" + arrowName.getKey() + "</text>)(.*)", "$1<a class=\"clickable\" href=\"#\">$2</a>$3");
        }
        capturedInputAndOutputs.put("Sequence diagram", new SvgWrapper(svg));
    }

    private String printFromStatement(String name, Map<String, String> arrowNamesCollector) {
        final Pattern pattern = Pattern.compile("(.*) " + FROM + " (.*)");
        final java.util.regex.Matcher matcher = pattern.matcher(name);
        matcher.matches();
        final String what = matcher.group(1).trim();
        final String who = matcher.group(2).trim();
        arrowNamesCollector.put(what, name.trim());
        return who + " ->> " + subject + ":" + what;
    }

    private String printToStatement(String name, Map<String, String> arrowNamesCollector) {
        final Pattern pattern = Pattern.compile("(.*) " + TO + " (.*)");
        final java.util.regex.Matcher matcher = pattern.matcher(name);
        matcher.matches();
        final String what = matcher.group(1).trim();
        final String who = matcher.group(2).trim();
        arrowNamesCollector.put(what, name.trim());
        return subject + " ->> " + who + ":" + what;
    }

    public static Content getHeaderContentForModalWindows() {
        return new Content(SequenceDiagramGenerator.class.getResource("dialogScriptHeaderContent.html"));
    }

}

