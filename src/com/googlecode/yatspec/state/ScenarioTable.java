package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Callable1;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.state.ScenarioTableHeader.toScenarioTableHeader;

@SuppressWarnings("unused")
public class ScenarioTable {
    private List<ScenarioTableHeader> headers = new ArrayList<ScenarioTableHeader>();
    private List<List<String>> rows = new ArrayList<List<String>>();

    public List<ScenarioTableHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        if(headers != null)
            this.headers = sequence(headers).map(toScenarioTableHeader()).toList();
    }

    public void addRow(List<String> values) {
        rows.add(values);
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public boolean isEmpty() {
        return headers.size() == 0 && rows.size() == 0;
    }
}
