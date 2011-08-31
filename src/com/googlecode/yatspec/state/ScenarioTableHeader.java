package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Value;

public class ScenarioTableHeader implements Value<String> {
    private final String header;

    public ScenarioTableHeader(String header) {
        this.header = header;
    }

    @Override
    public String value() {
        return header;
    }

    public static Callable1<? super String, ScenarioTableHeader> toScenarioTableHeader() {
        return new Callable1<String, ScenarioTableHeader>() {
            @Override
            public ScenarioTableHeader call(String value) throws Exception {
                return new ScenarioTableHeader(value);
            }
        };
    }


}
