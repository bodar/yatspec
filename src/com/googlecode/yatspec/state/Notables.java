package com.googlecode.yatspec.state;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.rendering.Renderer;
import com.googlecode.yatspec.rendering.NotesRenderer;

import java.io.NotSerializableException;

final class Notables {
    private Notables() {
    }

    public static String getNotesValue(Notes annotation) throws Exception {
        return getNotesValue(annotation, null);
    }

    public static String getNotesValue(Notes notes, Renderer<Notes> renderer) throws Exception {
        return renderer != null ? renderer.render(notes) : new NotesRenderer().render(notes);
    }
}