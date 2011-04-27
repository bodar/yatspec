package com.googlecode.yatspec.rendering;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import static com.googlecode.totallylazy.Strings.escapeXml;

public class DocumentRenderer implements Renderer<Document> {
    public String render(Document document) throws Exception {
        return escapeXml(prettyFormat(document));
    }

    private String prettyFormat(Document document) {
        return new XMLOutputter(Format.getPrettyFormat()).outputString(document);
    }
}
