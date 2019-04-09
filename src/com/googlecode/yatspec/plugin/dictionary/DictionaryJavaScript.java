package com.googlecode.yatspec.plugin.dictionary;

import com.googlecode.yatspec.rendering.ContentAtUrl;

public class DictionaryJavaScript extends ContentAtUrl {

    public DictionaryJavaScript() {
        super(DictionaryJavaScript.class.getResource("dictionary.js"));
    }
}
