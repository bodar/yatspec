package com.googlecode.yatspec.junit;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequences;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@YatspecAnnotation
public @interface Notes {
    String value();

    class methods {
        public static Option<Notes> notes(Iterable<Annotation> annotations) {
            return Sequences.sequence(annotations).safeCast(Notes.class).headOption();
        }
    }
}
