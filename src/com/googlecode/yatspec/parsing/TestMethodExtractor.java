package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.annotation.AnnotationValueList;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.lines;
import static com.googlecode.yatspec.parsing.TestParser.name;

public class TestMethodExtractor implements Callable1<Pair<JavaMethod, Method>, TestMethod> {
    public static TestMethodExtractor extractTestMethod() throws IOException {
        return new TestMethodExtractor();
    }

    public TestMethod call(Pair<JavaMethod, Method> pair) {
        return toTestMethod(pair.first(), pair.second());
    }

    public TestMethod toTestMethod(JavaMethod javaMethod, Method method) {
        final String name = javaMethod.getName();

        final JavaSource source = new JavaSource(javaMethod.getSourceCode());
        final ScenarioTable scenarioTable = getScenarioTable(javaMethod);
        return new TestMethod(method, name, source, scenarioTable);
    }

    private String lineSeperator() {
        return System.getProperty("line.separator");
    }

    @SuppressWarnings({"unchecked"})
    private ScenarioTable getScenarioTable(JavaMethod method) {
        ScenarioTable table = new ScenarioTable();
        table.setHeaders(getNames(method.getParameters()));

        final Sequence<Annotation> rows = getRows(method);
        for (Annotation row : rows) {
            List<String> values = (List<String>) row.getProperty("value").getParameterValue();
                table.addRow(sequence(values).map(replaceQuotes()).toList());
        }
        return table;
    }

    private Callable1<? super String, String> replaceQuotes() {
        return new Callable1<String, String>() {
            @Override
            public String call(String valueWithQuotes) throws Exception {
                return valueWithQuotes.replace("\"", "").trim();
            }
        };
    }

    private Sequence<Annotation> getRows(JavaMethod method) {
        return sequence(method.getAnnotations()).filter(where(name(), is(Table.class.getName()))).flatMap(rows());
    }

    private Callable1<? super Annotation, Iterable<Annotation>> rows() {
        return new Callable1<Annotation, Iterable<Annotation>>() {
            @Override
            public Iterable<Annotation> call(Annotation annotation) throws Exception {
                return ((AnnotationValueList)annotation.getProperty("value")).getValueList();
            }
        };
    }

    private List<String> getNames(JavaParameter[] javaParameters) {
        return sequence(javaParameters).map(getName()).toList();
    }

    private Callable1<JavaParameter, String> getName() {
        return new Callable1<JavaParameter, String>() {
            public String call(JavaParameter javaParameter) {
                return javaParameter.getName();
            }
        };
    }

}
