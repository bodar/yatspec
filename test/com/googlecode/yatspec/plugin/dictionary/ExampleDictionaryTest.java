package com.googlecode.yatspec.plugin.dictionary;

import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

@RunWith(SpecRunner.class)
public class ExampleDictionaryTest implements WithCustomResultListeners {

    @Test
    public void theDictionaryWordsAreHighlightedTest() {
        theWordYatspecWillBeHighlightedAndHoveringWillShowTheDefinition();
    }

    private void theWordYatspecWillBeHighlightedAndHoveringWillShowTheDefinition() {
        // just for the wording
    }

    @Override
    public Iterable<SpecResultListener> getResultListeners() {
        return Collections.<SpecResultListener>singletonList(
                new HtmlResultRenderer()
                        .withCustomScripts(new DictionaryJavaScript())
                        .withCustomHeaderContent(new DictionaryCss()));
    }
}
