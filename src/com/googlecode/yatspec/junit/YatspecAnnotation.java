package com.googlecode.yatspec.junit;

import com.googlecode.totallylazy.predicates.LogicalPredicate;

import java.lang.annotation.*;
import java.util.List;

import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Sequences.sequence;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface YatspecAnnotation {
    class methods {
        public static List<Annotation> yatspecAnnotations(Iterable<Annotation> annotations) {
            return sequence(annotations)
                    .filter(predicates.annotatedWith(YatspecAnnotation.class))
                    .toList();
        }

        public static boolean annotatedWith(Class<?> aClass, Class<? extends Annotation> annotation) {
            return sequence(aClass.getDeclaredAnnotations())
                    .exists(instanceOf(annotation));
        }
    }

    class predicates {
        public static <T extends Annotation> LogicalPredicate<T> annotatedWith(final Class<? extends Annotation> annotation) {
            return new LogicalPredicate<T>() {
                public boolean matches(Annotation instance) {
                    return methods.annotatedWith(instance.annotationType(), annotation);
                }
            };
        }

    }
}
