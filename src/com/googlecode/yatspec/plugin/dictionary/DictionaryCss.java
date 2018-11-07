package com.googlecode.yatspec.plugin.dictionary;

import com.googlecode.yatspec.rendering.ContentAtUrl;

public class DictionaryCss extends ContentAtUrl {

    public DictionaryCss() {
        super(DictionaryCss.class.getResource("dictionary.html"));
    }
}
