package com.googlecode.yatspec.plugin.sequencediagram;

public class PlantUmlSvgMarkupMunger  {
    public String munge(String svg) {
        return fixWidthAndHeight(makeSvgEmbeddable(svg));
    }

    private String fixWidthAndHeight(String svg) {
        svg = svg.replaceFirst("(.*)width:(\\d+);(.*)", "$1width:$2px;$3");
        svg = svg.replaceFirst("(.*)height:(\\d+);(.*)", "$1height:$2px;$3");
        return svg;
    }

    private String makeSvgEmbeddable(String svg) {
        svg = svg.replaceFirst("<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>", "");
        svg = svg.replaceFirst("position:absolute;top:0;left:0;", "");
        return svg;
    }

}
