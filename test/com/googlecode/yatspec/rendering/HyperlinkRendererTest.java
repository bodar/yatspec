package com.googlecode.yatspec.rendering;

import org.junit.Test;

import java.net.MalformedURLException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HyperlinkRendererTest {
    private Renderer<String> hyperlinkRenderer = new HyperlinkRenderer<String>("http://www.%s.com", new ToStringRenderer<String>());

    @Test
    public void shouldRenderHyperlink() throws Exception {
        assertThat(hyperlinkRenderer.render("somewhere"), equalTo("<a href=\"http://www.somewhere.com\" target=\"_blank\">somewhere</a>"));
    }

    @Test
    public void shouldNotRenderHyperlink() throws Exception {
        assertThat(hyperlinkRenderer.render(null), equalTo(""));
    }

    @Test(expected= MalformedURLException.class)
    public void shouldThrowExceptionOnInvalidUrl() throws Exception {
        new HyperlinkRenderer<String>("doesNotExist%s", new ToStringRenderer<String>()).render("somewhere");
    }
}