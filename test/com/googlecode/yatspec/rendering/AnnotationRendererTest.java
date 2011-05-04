package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.junit.Notes;
import org.junit.Test;

import static com.googlecode.yatspec.rendering.Annotations.notesProxyInstance;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AnnotationRendererTest {
    @Test
    public void shouldRenderAnnotation() throws Exception {
        EnhancedStringTemplateGroup stringTemplateGroup = new EnhancedStringTemplateGroup("name");
        stringTemplateGroup.registerRenderer(Notes.class, new NotesRenderer());
        AnnotationRenderer annotationRenderer = new AnnotationRenderer(stringTemplateGroup);
        Notes notes = notesProxyInstance();
        assertThat(annotationRenderer.toString(notes), equalTo(notes.value()));
    }
}
