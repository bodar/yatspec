package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;

import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Strings.escapeXml;

public class XmlStringRenderer implements AttributeRenderer {
    public String toString(Object o) {
        return option(escapeXml(o.toString())).getOrElse("");
    }

    public String toString(Object o, String formatName) {
        return toString(o);
    }
}
