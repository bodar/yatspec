package com.googlecode.yatspec.rendering;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import static com.googlecode.totallylazy.Strings.escapeXml;
import static org.jdom.output.Format.getPrettyFormat;

public class DocumentRenderer implements Renderer<Document> {
    public String render(Document document) throws Exception {
        return escapeXml(prettyFormat(document));
    }

    private static String prettyFormat(Document document) {
        return new XMLOutputter(getPrettyFormat()).outputString(document);
    }
}
