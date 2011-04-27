package com.googlecode.yatspec.state;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.state.TestMethod.wordify;

@SuppressWarnings("unused")
public class ScenarioTable {
    private List<String> headers = new ArrayList<String>();
    private List<List<String>> rows = new ArrayList<List<String>>();

    public List<String> getHeaders() {
        return headers;
    }

    public List<String> getDisplayHeaders() {
        return sequence(headers).map(wordify()).toList();
    }

    public void setHeaders(List<String> headers) {
        if(headers != null)
            this.headers = headers;
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
