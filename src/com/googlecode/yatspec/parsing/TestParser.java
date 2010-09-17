package com.googlecode.yatspec.parsing;

import com.googlecode.yatspec.parsing.Files;
import com.googlecode.yatspec.state.TestMethod;
import jedi.filters.NotNullFilter;
import jedi.functional.Filter;
import jedi.functional.Functor;
import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.parsers.Java15Parser;
import org.apache.commons.io.IOUtils;
import org.jaxen.JaxenException;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static jedi.functional.Coercions.list;
import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.select;
import static com.googlecode.yatspec.parsing.TestMethodExtractor.extractTestMethod;
import static jedi.functional.FunctionalPrimitives.zip;

public class TestParser {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static List<TestMethod> parseTestMethods(Class aClass) throws Exception {
        final File javaSource = getJavaSourceFile(aClass);
        final InputStream stream = new FileInputStream(javaSource);
        final String wholeFile = IOUtils.toString(stream);

        final ASTCompilationUnit classAST = getClassAST(wholeFile);
        final List methodASTs = getMethodAST(classAST);
        final List methods = getMethods(aClass);
        final List zipped = zip(list(methodASTs, methods));

        return select(collect(zipped, extractTestMethod(wholeFile)), notNull());
    }

    private static ASTCompilationUnit getClassAST(String wholeFile) {
        final Java15Parser parser = new Java15Parser();
        parser.setExcludeMarker("//"); // exclude comments
        return (ASTCompilationUnit) parser.parse(new StringReader(wholeFile));
    }

    private static List<Method> getMethods(Class aClass) {
        return select(asList(aClass.getMethods()), new Filter<Method>() {
            public Boolean execute(Method method) {
                return method.getAnnotation(Test.class) != null;
            }
        });
    }


    private static List<ASTMethodDeclaration>  getMethodAST(ASTCompilationUnit classAST) throws JaxenException {
        return classAST.findChildNodesWithXPath("//MethodDeclaration[preceding-sibling::Annotation/MarkerAnnotation/Name[@Image='Test']]");
    }

    private static NotNullFilter notNull() {
        return new NotNullFilter();
    }

    private static File getJavaSourceFile(Class testClass) {
        final String filename = getPath(testClass.getSimpleName()) + ".java";
        return Files.find(filename);
    }

    public static String getPath(String testClassName) {
        return testClassName.replaceAll("\\.", "/");
    }
}
