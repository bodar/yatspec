package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;

import static org.apache.commons.lang.StringEscapeUtils.escapeXml;
import static org.apache.commons.lang.StringUtils.defaultString;

public class XmlStringRenderer implements AttributeRenderer {
    public String toString(Object o) {
        return defaultString(escapeXml(o.toString()));
    }

    public String toString(Object o, String formatName) {
        return toString(o);
    }
}
