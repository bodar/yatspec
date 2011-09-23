package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.state.TestMethod;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import static com.googlecode.totallylazy.Files.path;
import static com.googlecode.totallylazy.Files.recursiveFiles;
import static com.googlecode.totallylazy.Files.workingDirectory;
import static com.googlecode.totallylazy.Methods.annotation;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.endsWith;
import static com.googlecode.yatspec.parsing.Files.toJavaPath;
import static com.googlecode.yatspec.parsing.TestMethodExtractor.extractTestMethod;

public class TestParser {

    public static List<TestMethod> parseTestMethods(Class aClass) throws Exception {
        final Sequence<Method> methods = getMethods(aClass);
        return collectTestMethods(aClass, methods).toList();
    }

    private static Sequence<TestMethod> collectTestMethods(Class aClass, Sequence<Method> methods) throws IOException {
        final Option<JavaClass> javaClass = getJavaClass(aClass);
        if (javaClass.isEmpty()) {
            return empty();
        }

        Sequence<TestMethod> myTestMethods = getMethods(javaClass.get()).zip(methods).map(extractTestMethod());
        Sequence<TestMethod> parentTestMethods = collectTestMethods(aClass.getSuperclass(), methods);

        return myTestMethods.join(parentTestMethods);
    }

    private static Option<JavaClass> getJavaClass(final Class aClass) throws IOException {
        return getJavaSourceFile(aClass).map(asJavaClass(aClass));
    }

    private static Callable1<File, JavaClass> asJavaClass(final Class aClass) {
        return new Callable1<File, JavaClass>() {
            @Override
            public JavaClass call(File file) throws Exception {
                JavaDocBuilder builder = new JavaDocBuilder();
                builder.addSource(file);
                return builder.getClassByName(aClass.getName());
            }
        };
    }

    private static Sequence<Method> getMethods(Class aClass) {
        return sequence(aClass.getMethods()).filter(where(annotation(Test.class), notNullValue()));
    }

    @SuppressWarnings("unchecked")
    private static Sequence<JavaMethod> getMethods(JavaClass javaClass) {
        return sequence(javaClass.getMethods()).filter(where(annotations(), contains(Test.class)));
    }

    private static Option<File> getJavaSourceFile(Class clazz) {
        return recursiveFiles(workingDirectory()).find(where(path(), endsWith(toJavaPath(clazz))));
    }

    private static Predicate<? super Sequence<Annotation>> contains(final Class aClass) {
        return new Predicate<Sequence<Annotation>>() {
            @Override
            public boolean matches(Sequence<Annotation> annotations) {
                return annotations.exists(where(name(), is(aClass.getName())));
            }
        };
    }

    public static Callable1<? super Annotation, String> name() {
        return new Callable1<Annotation, String>() {
            @Override
            public String call(Annotation annotation) throws Exception {
                return annotation.getType().getFullyQualifiedName();
            }
        };
    }

    public static Callable1<? super JavaMethod, Sequence<Annotation>> annotations() {
        return new Callable1<JavaMethod, Sequence<Annotation>>() {
            @Override
            public Sequence<Annotation> call(JavaMethod javaMethod) throws Exception {
                return sequence(javaMethod.getAnnotations());
            }
        };
    }


}
