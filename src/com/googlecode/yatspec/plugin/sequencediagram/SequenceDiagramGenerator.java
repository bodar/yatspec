package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.state.givenwhenthen.CapturedInputAndOutputs;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.sourceforge.plantuml.FileFormat.SVG;

public class SequenceDiagramGenerator {

    private final CapturedInputAndOutputs capturedInputAndOutputs;
    private final PlantUmlSvgMarkupMunger svgMarkupMunger = new PlantUmlSvgMarkupMunger();
    private final SequenceDiagramHyperlinker sequenceDiagramHyperlinker = new SequenceDiagramHyperlinker();

    private StringBuffer optionalPlantUmlCollector;

    private PlantUmlMarkupGenerator plantUmlMarkupGenerator;

    public SequenceDiagramGenerator(CapturedInputAndOutputs capturedInputAndOutputs) {
        this(capturedInputAndOutputs, PlantUmlMarkupGenerator.DEFAULT_SUBJECT);
    }

    public SequenceDiagramGenerator(CapturedInputAndOutputs capturedInputAndOutputs, String subject) {
        this.capturedInputAndOutputs = capturedInputAndOutputs;
        plantUmlMarkupGenerator = new PlantUmlMarkupGenerator(capturedInputAndOutputs, subject);
    }

    public void logPlantUmlMarkupTo(StringBuffer plantUmlCollector) {
        this.optionalPlantUmlCollector = plantUmlCollector;
    }

    public void generateSequenceDiagram() {
        List<SequenceDiagramMessage> messagesCollector = new ArrayList<SequenceDiagramMessage>();
        List<String> actorNamesCollector = new ArrayList<String>();
        String plantUmlMarkup = plantUmlMarkupGenerator.collectPlantUmlMarkup(messagesCollector, actorNamesCollector);
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

    public static Content getHeaderContentForModalWindows() {
        return new Content(SequenceDiagramGenerator.class.getResource("dialogScriptHeaderContent.html"));
    }
}