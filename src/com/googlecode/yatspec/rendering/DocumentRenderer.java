package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;
import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class DocumentRenderer implements AttributeRenderer {
    public String toString(Object o) {
        Document document = (Document) o;
        return StringEscapeUtils.escapeXml(prettyFormat(document));
    }

    private String prettyFormat(Document document) {
        return new XMLOutputter(Format.getPrettyFormat()).outputString(document);
    }

    public String toString(Object o, String formatName) {
        return toString(o);
    }
}
