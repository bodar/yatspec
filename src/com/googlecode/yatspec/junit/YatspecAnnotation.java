package com.googlecode.yatspec.junit;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface YatspecAnnotation {
    class methods {
        public static List<Annotation> filter(Annotation[] annotations) {
            List<Annotation> results = new ArrayList<Annotation>();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> aClass = annotation.annotationType();
                if(!sequence(aClass.getDeclaredAnnotations()).safeCast(YatspecAnnotation.class).isEmpty()) {
                    results.add(annotation);
                }
            }
            return results;
        }
    }
}
