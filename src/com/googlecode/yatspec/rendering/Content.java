package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Strings;

import java.io.IOException;
import java.net.URL;

public class Content {
    private final URL url;

    public Content(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        try {
            return Strings.toString(url.openStream());
        } catch (IOException e) {
            return e.toString();
        }
    }
}
