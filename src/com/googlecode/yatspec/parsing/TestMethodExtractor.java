package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.annotation.AnnotationValue;
import com.thoughtworks.qdox.model.annotation.AnnotationValueList;
import com.thoughtworks.qdox.model.annotation.EvaluatingVisitor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.TestParser.name;

public class TestMethodExtractor {
    public TestMethod toTestMethod(Class aClass, JavaMethod javaMethod, Method method) {
        final String name = javaMethod.getName();

        final JavaSource source = new JavaSource(javaMethod.getSourceCode());
        final ScenarioTable scenarioTable = getScenarioTable(javaMethod);
        return new TestMethod(aClass, method, name, source, scenarioTable);
    }

    @SuppressWarnings({"unchecked"})
    private ScenarioTable getScenarioTable(JavaMethod method) {
        ScenarioTable table = new ScenarioTable();
        table.setHeaders(getNames(method.getParameters()));

        final Sequence<Annotation> rows = getRows(method);
        for (Annotation row : rows) {
            List<String> values = getRowValues(row);
            table.addRow(sequence(values).map(Strings.trim()).toList());
        }
        return table;
    }

    private List<String> getRowValues(Annotation row) {
        final AnnotationValue value = row.getProperty("value");
        final EvaluatingVisitor annotationVisitor = new EvaluatingVisitor() {
            @Override
            protected Object getFieldReferenceValue(JavaField javaField) {
                Type type = javaField.getType();
                if (type.isA(new Type(String.class.getName()))) {
                    return javaField.getInitializationExpression().replace("\"", "");
                } else {
                    return value.getParameterValue();
                }
            }
        };

        final Object parameterValue = value.accept(annotationVisitor);

        if (parameterValue instanceof List) {
            return (List<String>) parameterValue;
        } else {
            return Arrays.asList(parameterValue.toString());
        }
    }

    private Sequence<Annotation> getRows(JavaMethod method) {
        return sequence(method.getAnnotations()).filter(where(name(), is(Table.class.getName()))).flatMap(rows());
    }

    private Callable1<? super Annotation, Iterable<Annotation>> rows() {
        return new Callable1<Annotation, Iterable<Annotation>>() {
            @Override
            public Iterable<Annotation> call(Annotation annotation) throws Exception {
                return ((AnnotationValueList) annotation.getProperty("value")).getValueList();
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
