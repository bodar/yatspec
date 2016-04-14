package com.googlecode.yatspec.plugin.sequencediagram;

import com.googlecode.yatspec.rendering.Content;
import com.googlecode.yatspec.rendering.ContentAtUrl;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static com.googlecode.totallylazy.Sequences.sequence;
import static net.sourceforge.plantuml.FileFormat.SVG;

public class SequenceDiagramGenerator {

    private StringBuffer optionalPlantUmlCollector;

    public SvgWrapper generateSequenceDiagram(Iterable<SequenceDiagramMessage> messages) {
        String plantUmlMarkup = new PlantUmlMarkupGenerator().generateMarkup(sequence(messages));
        makePlantUmlAvailableToAnyRegisteredCollector(plantUmlMarkup);

        return new SvgWrapper(prettyPrint(createSvg(plantUmlMarkup)));
    }

    private String prettyPrint(String xml) {
        try {
            SAXBuilder sb = new SAXBuilder();
            Document doc = sb.build(new StringReader(xml));
            StringWriter stringWriter = new StringWriter();
            new XMLOutputter(Format.getPrettyFormat()).output(doc, stringWriter);
            return stringWriter.toString();
        } catch (JDOMException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void logPlantUmlMarkupTo(StringBuffer plantUmlCollector) {
        this.optionalPlantUmlCollector = plantUmlCollector;
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
        return new ContentAtUrl(SequenceDiagramGenerator.class.getResource("dialogScriptHeaderContent.html"));
    }
}