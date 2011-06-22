package com.googlecode.yatspec.plugin.sequencediagram;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class PlantUmlSvgMarkupMungerTest {
    private static final String plantUmlSvg =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
            "<svg xmlns=\"http://www.w3.org/2000/svg\" style=\"width:359;height:255;\" version=\"1.1\">\n" +
            "    <defs/>\n" +
            "    <g>\n" +
            "        <line/>\n" +
            "        <rect/>\n" +
            "        <text font-size=\"9\">barney</text>\n" +
            "        <rect/>\n" +
            "        <text font-size=\"10\">fred</text>\n" +
            "        <text font-size=\"11\">open</text>\n" +
            "        <line/>\n" +
            "        <text font-size=\"12\">close</text>\n" +
            "        <line/>\n" +
            "        <text font-size=\"13\">open</text>\n" +
            "    </g>\n" +
            "</svg>";

    @Test
    public void removesXmlDeclaration() {
        final PlantUmlSvgMarkupMunger munger = new PlantUmlSvgMarkupMunger();
        assertThat(munger.munge(plantUmlSvg), not(containsString("<?xml version=")));
    }
    
    @Test
    public void addsUnitsToWidthAndHeight() {
        final PlantUmlSvgMarkupMunger munger = new PlantUmlSvgMarkupMunger();
        assertThat(munger.munge(plantUmlSvg), containsString("width:359px;"));
        assertThat(munger.munge(plantUmlSvg), containsString("height:255px;"));
    }
}
