package com.googlecode.yatspec.rendering;

import org.antlr.stringtemplate.AttributeRenderer;
import com.googlecode.totallylazy.Strings;
import static com.googlecode.totallylazy.Option.option;

public class XmlStringRenderer implements AttributeRenderer {
    public String toString(Object o) {
        return option(Strings.escapeXml(o.toString())).getOrElse("");
    }

    public String toString(Object o, String formatName) {
        return toString(o);
    }
}
