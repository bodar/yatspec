package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.junit.LinkingNote;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.junit.SpecRunner.outputDirectory;
import static com.googlecode.yatspec.parsing.Text.wordify;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlResultFile;
import static java.lang.String.format;

public class LinkingNoteRenderer implements Renderer<LinkingNote>{

    @Override
    public String render(LinkingNote linkingNoteNotes) throws Exception {
        return format(linkingNoteNotes.message(), (Object[])sequence(linkingNoteNotes.links()).map(link()).toArray(String.class));
    }

    private Callable1<Class, String> link() {
        return new Callable1<Class, String>() {
            @Override
            public String call(Class aClass) throws Exception {
                return format("<a href='%s'>%s</a>", htmlResultFile(outputDirectory(), aClass), wordify(aClass.getSimpleName()));
            }
        };
    }
}
