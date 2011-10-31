package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.junit.Notes;

public class Annotations {
    private Annotations() {
    }

    public static Class notesProxyClass() throws Exception {
        return notesProxyInstance().getClass();
    }

    @Notes("")
    public static Notes notesProxyInstance() throws Exception {
        final Notes [] instance = new Notes[1];
        new Object() {{
            instance[0] = this.getClass().getEnclosingMethod().getAnnotation(Notes.class);
        }};

        return instance [0];
    }
}
