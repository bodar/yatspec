package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;
import org.apache.commons.lang.StringEscapeUtils;

public class XmlStringRenderer implements AttributeRenderer {
    public String toString(Object o) {
        return StringEscapeUtils.escapeXml(o.toString());
    }

    public String toString(Object o, String formatName) {
        return toString(o);
    }
}
