package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.junit.Notes;

public class NotesRenderer implements Renderer<Notes>{
    @Override
    public String render(Notes notes) throws Exception {
        return notes.value();
    }
}
