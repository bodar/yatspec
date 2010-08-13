package com.googlecode.yatspec.rendering;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class DocumentRenderer implements Renderer<Document> {
    public String render(Document document) throws Exception {
        return StringEscapeUtils.escapeXml(prettyFormat(document));
    }

    private String prettyFormat(Document document) {
        return new XMLOutputter(Format.getPrettyFormat()).outputString(document);
    }
}
