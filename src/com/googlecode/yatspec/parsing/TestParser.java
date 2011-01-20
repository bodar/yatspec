package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.yatspec.state.TestMethod;
import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.parsers.Java15Parser;
import org.jaxen.JaxenException;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.util.List;

import static com.googlecode.totallylazy.Predicates.notNull;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.TestMethodExtractor.extractTestMethod;

public class TestParser {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static List<TestMethod> parseTestMethods(Class aClass) throws Exception {
        final File javaSource = getJavaSourceFile(aClass);
        final InputStream stream = new FileInputStream(javaSource);
        final String wholeFile = Strings.toString(stream);

        final ASTCompilationUnit classAST = getClassAST(wholeFile);
        final Sequence<ASTMethodDeclaration> methodASTs = getMethodAST(classAST);
        final Sequence<Method> methods = getMethods(aClass);

        return methodASTs.zip(methods).map(extractTestMethod(wholeFile)).filter(notNull(TestMethod.class)).toList();
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


    private static Sequence<ASTMethodDeclaration> getMethodAST(ASTCompilationUnit classAST) throws JaxenException {
        return sequence(classAST.findChildNodesWithXPath("//MethodDeclaration[preceding-sibling::Annotation/MarkerAnnotation/Name[@Image='Test']]"));
    }

    private static File getJavaSourceFile(Class testClass) {
        final String filename = getPath(testClass.getSimpleName()) + ".java";
        return Files.find(filename);
    }

    public static String getPath(String testClassName) {
        return testClassName.replaceAll("\\.", "/");
    }
}
