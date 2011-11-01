package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Value;

import java.util.List;

public class JavaSource implements Value<String> {
    private static final String COMMA_OR_OPENING_BRACKET = "(\\s*[\\(\\,\"]\\s*)";
    private static final String COMMA_OR_CLOSING_BRACKET = "(\\s*[\\,\\)\"]\\s*)";
    private final String value;

    public JavaSource(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    public JavaSource replace(final List<String> oldValues, final List<String> newValues) {
        String result = value();
        for (int i = 0; i < oldValues.size(); i++) {
            String header = oldValues.get(i);
            String value = newValues.get(i);
            result = result.replaceAll(COMMA_OR_OPENING_BRACKET + header + COMMA_OR_CLOSING_BRACKET, "$1" + displayValue(value) + "$2");
        }
        return new JavaSource(result);
    }

    private static String displayValue(String value) {
        if (value.matches("[A-Z0-9]*")) {
            return value;
        }
        return "\"" + value + "\"";
    }

    @Override
    public String toString() {
        return value();
    }
}
