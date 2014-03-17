package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.annotation.AnnotationAdd;
import com.thoughtworks.qdox.model.annotation.AnnotationAnd;
import com.thoughtworks.qdox.model.annotation.AnnotationCast;
import com.thoughtworks.qdox.model.annotation.AnnotationConstant;
import com.thoughtworks.qdox.model.annotation.AnnotationDivide;
import com.thoughtworks.qdox.model.annotation.AnnotationEquals;
import com.thoughtworks.qdox.model.annotation.AnnotationExclusiveOr;
import com.thoughtworks.qdox.model.annotation.AnnotationFieldRef;
import com.thoughtworks.qdox.model.annotation.AnnotationGreaterEquals;
import com.thoughtworks.qdox.model.annotation.AnnotationGreaterThan;
import com.thoughtworks.qdox.model.annotation.AnnotationLessEquals;
import com.thoughtworks.qdox.model.annotation.AnnotationLessThan;
import com.thoughtworks.qdox.model.annotation.AnnotationLogicalAnd;
import com.thoughtworks.qdox.model.annotation.AnnotationLogicalNot;
import com.thoughtworks.qdox.model.annotation.AnnotationLogicalOr;
import com.thoughtworks.qdox.model.annotation.AnnotationMinusSign;
import com.thoughtworks.qdox.model.annotation.AnnotationMultiply;
import com.thoughtworks.qdox.model.annotation.AnnotationNot;
import com.thoughtworks.qdox.model.annotation.AnnotationNotEquals;
import com.thoughtworks.qdox.model.annotation.AnnotationOr;
import com.thoughtworks.qdox.model.annotation.AnnotationParenExpression;
import com.thoughtworks.qdox.model.annotation.AnnotationPlusSign;
import com.thoughtworks.qdox.model.annotation.AnnotationQuery;
import com.thoughtworks.qdox.model.annotation.AnnotationRemainder;
import com.thoughtworks.qdox.model.annotation.AnnotationShiftLeft;
import com.thoughtworks.qdox.model.annotation.AnnotationShiftRight;
import com.thoughtworks.qdox.model.annotation.AnnotationSubtract;
import com.thoughtworks.qdox.model.annotation.AnnotationTypeRef;
import com.thoughtworks.qdox.model.annotation.AnnotationUnsignedShiftRight;
import com.thoughtworks.qdox.model.annotation.AnnotationValue;
import com.thoughtworks.qdox.model.annotation.AnnotationValueList;
import com.thoughtworks.qdox.model.annotation.AnnotationVisitor;
import com.thoughtworks.qdox.model.annotation.EvaluatingVisitor;
import com.thoughtworks.qdox.model.annotation.RecursiveAnnotationVisitor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                //TODO Use reflection to get the constant
                return null;
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
