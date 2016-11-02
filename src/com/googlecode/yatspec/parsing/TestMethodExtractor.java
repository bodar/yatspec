package com.googlecode.yatspec.parsing;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.yatspec.junit.Collapsible;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.Section;
import com.googlecode.yatspec.state.TestMethod;
import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.annotation.AnnotationValue;
import com.thoughtworks.qdox.model.annotation.AnnotationValueList;
import com.thoughtworks.qdox.model.annotation.EvaluatingVisitor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.TestParser.name;
import static java.util.Collections.singletonList;

public class TestMethodExtractor {
    public TestMethod toTestMethod(Class aClass, JavaMethod javaMethod, Method method) {
        final String name = javaMethod.getName();

        final JavaSource source = new JavaSource(javaMethod.getSourceCode());
        final ScenarioTable scenarioTable = getScenarioTable(javaMethod);

        try {
            List<Statement> stmts = JavaParser.parseBlock("{" + javaMethod.getSourceCode() + "}").getStmts();
            List<Section> sections = new ArrayList<Section>();
            for (Statement stmt : stmts) {
                List<Node> childrenNodes = stmt.getChildrenNodes();
                for (Node childrenNode : childrenNodes) {
                    if (childrenNode instanceof MethodCallExpr) {
                        MethodCallExpr methodCall = (MethodCallExpr) childrenNode;
                        String methodName = methodCall.getName();
                        List<Method> methods = sequence(aClass.getDeclaredMethods()).filter(methodName(methodName)).toList();
                        if (methods.size() == 0) {
                            // TODO skip???
                            continue;
                        } else if (methods.size() > 1) {
                            // TODO use javaMethod.getParameters() to find the right method here
                            throw new UnsupportedOperationException("not yet implemented: " + methods.size() + " " + methodName);
                        }
                        Method method1 = methods.get(0);
                        if (sequence(method1.getAnnotations()).map(annotationType()).contains(Collapsible.class)) {
                            JavaSource specification = new JavaSource(TestParser.getJavaClass(aClass).get().getMethod(methodName, null, false).getSourceCode());
                            sections.add(new Section(new JavaSource(methodName), specification, true));
                        }
                    }
                }
            }
            if (!sections.isEmpty()) {
                return new TestMethod(aClass, method, name, scenarioTable, sections);
            }
        } catch (ParseException e) {
            throw new UnsupportedOperationException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new TestMethod(aClass, method, name, scenarioTable, singletonList(new Section(source, false)));
    }

    private Callable1<java.lang.annotation.Annotation, Class<? extends java.lang.annotation.Annotation>> annotationType() {
        return new Callable1<java.lang.annotation.Annotation, Class<? extends java.lang.annotation.Annotation>>() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> call(java.lang.annotation.Annotation annotation) throws Exception {
                return annotation.annotationType();
            }
        };
    }

    private Predicate<Method> methodName(final String methodName) {
        return new Predicate<Method>() {
            @Override
            public boolean matches(Method other) {
                return other.getName().equals(methodName);
            }
        };
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
