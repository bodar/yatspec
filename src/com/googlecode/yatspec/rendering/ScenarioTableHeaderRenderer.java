package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.ScenarioTableHeader;

import static com.googlecode.yatspec.parsing.Text.wordify;

public class ScenarioTableHeaderRenderer implements Renderer<ScenarioTableHeader>{
    @Override
    public String render(ScenarioTableHeader header) throws Exception {
        return wordify(header.value());
    }
}
