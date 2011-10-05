package com.googlecode.yatspec.plugin.sequencediagram;

public class SvgWrapper {
    private final String svg;

    public SvgWrapper(String svg) {
        this.svg = svg;
    }

    public String toString() {
        return svg;
    }
}

