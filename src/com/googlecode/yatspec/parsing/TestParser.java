package com.googlecode.yatspec.parsing;

import com.googlecode.yatspec.parsing.Files;
import com.googlecode.yatspec.state.TestMethod;
import jedi.filters.NotNullFilter;
import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.parsers.Java15Parser;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.select;
import static com.googlecode.yatspec.parsing.TestMethodExtractor.extractTestMethod;

public class TestParser {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static List<TestMethod> parseTestMethods(Class klass) throws Exception {
        final File javaSource = getJavaSourceFile(klass);
        final InputStream stream = new FileInputStream(javaSource);
        final String wholeFile = IOUtils.toString(stream);

        final Java15Parser parser = new Java15Parser();
        parser.setExcludeMarker("//"); // exclude comments
        final ASTCompilationUnit aClass = (ASTCompilationUnit) parser.parse(new StringReader(wholeFile));
        final List<ASTMethodDeclaration> astMethodDeclarationList = aClass.findChildNodesWithXPath("//MethodDeclaration[preceding-sibling::Annotation/MarkerAnnotation/Name[@Image='Test']]");
        return select(collect(astMethodDeclarationList, extractTestMethod(wholeFile)), notNull());
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
