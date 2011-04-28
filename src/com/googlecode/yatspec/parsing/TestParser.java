package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.yatspec.state.TestMethod;
import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.parsers.Java15Parser;
import org.jaxen.JaxenException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.List;

import static com.googlecode.totallylazy.Files.path;
import static com.googlecode.totallylazy.Files.recursiveFiles;
import static com.googlecode.totallylazy.Methods.annotation;
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

    private static Sequence<TestMethod> collectTestMethods(Class aClass, Sequence<Method> methods) throws JaxenException, IOException {
        final Option<File> javaSource = getJavaSourceFile(aClass);
        if (javaSource.isEmpty()) {
            return empty();
        }

        File file = javaSource.get();
        final ASTCompilationUnit classAST = getClassAST(file);
        final Sequence<ASTMethodDeclaration> methodASTs = getMethodAST(classAST);

        Sequence<TestMethod> myTestMethods = methodASTs.zip(methods).map(extractTestMethod(file)).filter(notNullValue());
        Sequence<TestMethod> parentTestMethods = collectTestMethods(aClass.getSuperclass(), methods);

        return myTestMethods.join(parentTestMethods);
    }

    private static ASTCompilationUnit getClassAST(File file) throws IOException {
        return (ASTCompilationUnit) commentIgnoringParser().parse(new StringReader(Strings.toString(file)));
    }

    private static Java15Parser commentIgnoringParser() {
        final Java15Parser parser = new Java15Parser();
        parser.setExcludeMarker("//");
        return parser;
    }

    private static Sequence<Method> getMethods(Class aClass) {
        return sequence(aClass.getMethods()).filter(where(annotation(Test.class), notNullValue()));
    }

    @SuppressWarnings("unchecked")
    private static Sequence<ASTMethodDeclaration> getMethodAST(ASTCompilationUnit classAST) throws JaxenException {
        return sequence(classAST.findChildNodesWithXPath("//MethodDeclaration[preceding-sibling::Annotation/MarkerAnnotation/Name[@Image='Test']]"));
    }

    private static Option<File> getJavaSourceFile(Class clazz) {
        return recursiveFiles(userDirectory()).find(where(path(), endsWith(toJavaPath(clazz))));
    }

    private static File userDirectory() {
        return new File(System.getProperty("user.dir"));
    }

}
