package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ContentAtUrl implements Content {
    private final URL url;

    public ContentAtUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            return Strings.toString(inputStream);
        } catch (IOException e) {
            return e.toString();
        } finally {
            closeQuietly(inputStream);
        }
    }

    private static void closeQuietly(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
