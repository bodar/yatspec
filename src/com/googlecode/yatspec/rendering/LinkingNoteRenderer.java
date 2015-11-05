package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.yatspec.junit.LinkingNote;
import com.googlecode.yatspec.rendering.Renderer;

import java.io.File;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.Text.wordify;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlResultRelativePath;
import static java.lang.String.format;

public class LinkingNoteRenderer implements Renderer<LinkingNote> {

    private final Class<?> source;

    public LinkingNoteRenderer(Class<?> source) {
        this.source = source;
    }

    @Override
    public String render(LinkingNote linkingNoteNotes) throws Exception {
        return format(linkingNoteNotes.message(), (Object[])sequence(linkingNoteNotes.links()).map(link()).toArray(String.class));
    }

    private Callable1<Class, String> link() {
        return new Callable1<Class, String>() {
            @Override
            public String call(Class targetClass) throws Exception {
                return format("<a href='%s'>%s</a>", htmlResultFile(targetClass, source), wordify(targetClass.getSimpleName()));
            }
        };
    }

    private File htmlResultFile(Class resultClass, Class sourceClass) {
        return new File(getRootDirectoryPath(sourceClass) + htmlResultRelativePath(resultClass));
    }

    public String getRootDirectoryPath(Class sourceClass) {
        return sequence(htmlResultRelativePath(sourceClass).split("/"))
                .map(toParent())
                .toString("");
    }

    private Callable1<? super String, String> toParent() {
        return new Callable1<String, String>() {
            @Override
            public String call(String s) throws Exception {
                if (s.contains(".")) {
                    return "";
                }
                return "../";
            }
        };
    }
}
