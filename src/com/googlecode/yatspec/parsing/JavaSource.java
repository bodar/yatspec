package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Value;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class JavaSource implements Value<List<String>> {
    private final List<String> lines;

    public JavaSource(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public List<String> value() {
        return lines;
    }

    public JavaSource replace(final List<String> oldValues, final List<String> newValues) {
        return new JavaSource(sequence(lines).map(new Callable1<String, String>() {
            public String call(String specificationLine) {
                String result = specificationLine;
                for (int i = 0; i < oldValues.size(); i++) {
                    String header = oldValues.get(i);
                    String value = newValues.get(i);
                    result = result.replaceAll("([^\\w\"])" + header + "([^\\w\"])", "$1" + displayValue(value) + "$2" );
                }
                return result;
            }
        }).toList());
    }

    private static String displayValue(String value) {
        if (value.matches("[A-Z0-9]*")) {
            return value;
        }
        return "\"" + value + "\"";
    }




}
