package com.googlecode.yatspec.rendering;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import com.googlecode.totallylazy.Strings;

public class DocumentRenderer implements Renderer<Document> {
    public String render(Document document) throws Exception {
        return Strings.escapeXml(prettyFormat(document));
    }

    private String prettyFormat(Document document) {
        return new XMLOutputter(Format.getPrettyFormat()).outputString(document);
    }
}
