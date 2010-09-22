package com.googlecode.yatspec.state;

import com.googlecode.yatspec.parsing.Text;
import jedi.functional.Functor;

import java.util.ArrayList;
import java.util.List;

import static jedi.functional.FunctionalPrimitives.collect;

public class ScenarioTable {
    private List<String> headers = new ArrayList<String>();
    private List<List<String>> rows = new ArrayList<List<String>>();

    public List<String> getHeaders() {
        return headers;
    }

    public List<String> getDisplayHeaders() {
        return collect(headers, new Functor<String, String>(){
            public String execute(String header) {
                return Text.wordify(header);
            }
        });
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
