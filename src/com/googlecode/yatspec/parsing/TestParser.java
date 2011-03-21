package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.yatspec.state.TestMethod;
import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.parsers.Java15Parser;
import org.jaxen.JaxenException;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.List;

import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.Files.find;
import static com.googlecode.yatspec.parsing.TestMethodExtractor.extractTestMethod;

public class TestParser {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static List<TestMethod> parseTestMethods(Class aClass) throws Exception {
        final Sequence<Method> methods = getMethods(aClass);

        return collectTestMethods(aClass, methods).toList();
    }

    private static Sequence<TestMethod> collectTestMethods(Class aClass, Sequence<Method> methods) throws JaxenException, IOException {
        final File javaSource = getJavaSourceFile(aClass);
        if (javaSource == null) {
            return empty();
        }

        final InputStream stream = new FileInputStream(javaSource);
        final String wholeFile = Strings.toString(stream);

        final ASTCompilationUnit classAST = getClassAST(wholeFile);
        final Sequence<ASTMethodDeclaration> methodASTs = getMethodAST(classAST);

        Sequence<TestMethod> myTestMethods = methodASTs.zip(methods).map(extractTestMethod(wholeFile)).filter(notNullValue());
        Sequence<TestMethod> parentTestMethods = collectTestMethods(aClass.getSuperclass(), methods);

        return myTestMethods.join(parentTestMethods);
    }

    private static ASTCompilationUnit getClassAST(String wholeFile) {
        final Java15Parser parser = new Java15Parser();
        parser.setExcludeMarker("//"); // exclude comments
        return (ASTCompilationUnit) parser.parse(new StringReader(wholeFile));
    }

    private static Sequence<Method> getMethods(Class aClass) {
        return sequence(aClass.getMethods()).filter(hasTestAnnotation());
    }

    private static Predicate<Method> hasTestAnnotation() {
        return new Predicate<Method>() {
            public boolean matches(Method method) {
                return method.getAnnotation(Test.class) != null;
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static Sequence<ASTMethodDeclaration> getMethodAST(ASTCompilationUnit classAST) throws JaxenException {
        return sequence(classAST.findChildNodesWithXPath("//MethodDeclaration[preceding-sibling::Annotation/MarkerAnnotation/Name[@Image='Test']]"));
    }

    private static File getJavaSourceFile(Class clazz) {
        return find(parentDirectory(), javaSourceFilename(clazz));
    }

    private static File parentDirectory() {
        return new File(".");
    }

    private static String javaSourceFilename(Class testClass) {
        return getPath(testClass.getSimpleName()) + ".java";
    }


    public static String getPath(String className) {
        return className.replaceAll("\\.", "/");
    }
}
