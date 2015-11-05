package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.rendering.LinkingNoteRenderer;
import com.googlecode.yatspec.state.TestMethod;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.TestParser.parseTestMethods;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpecRunner.class)
public class LinkingNoteRendererTest {
    @Test
    @LinkingNote(message="Classes specified in the linking note should result in links to the resulted yatspec output eg. %s, %s", links = {String.class, Integer.class})
    public void shouldRenderLinkedNotesAsNotesWithLinksToTheYatspecOutputOfTheTestClassesSpecified() throws Exception {
        assertThat(theRenderedValueOfTheLinkingNoteOfThisMethod(),
                is("Classes specified in the linking note should result in links to the resulted yatspec output eg. <a href='../../../../java/lang/String.html'>String</a>, <a href='../../../../java/lang/Integer.html'>Integer</a>"));
    }

    private String theRenderedValueOfTheLinkingNoteOfThisMethod() throws Exception {
        final List<TestMethod> methods = parseTestMethods(getClass());
        return new LinkingNoteRenderer(LinkingNoteRendererTest.class).render(sequence(sequence(methods).first().getAnnotations()).safeCast(LinkingNote.class).head());
    }
}
