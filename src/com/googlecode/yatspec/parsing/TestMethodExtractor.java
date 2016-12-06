package com.googlecode.yatspec.parsing;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
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
import static com.googlecode.yatspec.parsing.TestParser.getJavaClass;
import static com.googlecode.yatspec.parsing.TestParser.name;
import static java.util.Collections.singletonList;

public class TestMethodExtractor {
    public TestMethod toTestMethod(Class testClass, JavaMethod javaTestMethod, Method reflectionTestMethod) {
        try {
            List<Statement> statementsInTestMethod = JavaParser.parseBlock("{" + javaTestMethod.getSourceCode() + "}").getStmts();
            List<Section> sectionsInTestMethod = findCollapsibleSections(testClass, statementsInTestMethod);
            if (!sectionsInTestMethod.isEmpty()) {
                return new TestMethod(testClass, reflectionTestMethod, javaTestMethod.getName(), getScenarioTable(javaTestMethod), sectionsInTestMethod);
            } else {
                return new TestMethod(testClass, reflectionTestMethod, javaTestMethod.getName(), getScenarioTable(javaTestMethod), singletonList(new Section(new JavaSource(javaTestMethod.getSourceCode()), false)));
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private List<Section> findCollapsibleSections(Class testClass, List<Statement> statementsInTestMethod) throws IOException {
        List<Section> sectionsInTestMethod = new ArrayList<Section>();
        for (Statement statement : statementsInTestMethod) {
            for (Node statementNode : statement.getChildrenNodes()) {
                if (statementNode instanceof MethodCallExpr) {
                    MethodCallExpr methodCall = (MethodCallExpr) statementNode;
                    String methodName = methodCall.getName();
                    // TODO improve @Collapsible: look for methods also in static imports and superclass as well as current class
                    List<Method> methods = sequence(testClass.getDeclaredMethods())
                            .filter(methodName(methodName))
                            .filter(numberOfArguments(methodCall.getArgs().size()))
                            .filter(argumentTypes(methodCall.getArgs()))
                            .toList();
                    if (methods.size() > 0) {
                        if (methods.size() > 1) {
                            throw new IllegalArgumentException("Collapsible methods have restrictions on how they can be used. Could not determine which method is to be collapsed. Found " + methods.size() + " methods with name '" + methodName + "' and number of arguments " + methodCall.getArgs().size() + ". Please rename one of the following collapsible methods: " + methods);
                        }
                        Method methodToCollapse = methods.get(0);
                        if (sequence(methodToCollapse.getAnnotations()).map(annotationType()).contains(Collapsible.class)) {
                            JavaMethod method = sequence(getJavaClass(testClass).get().getMethods())
                                    .filter(methodName2(methodCall.getName()))
                                    .filter(argumentTypes2(methodCall.getArgs()))
                                    .first();
                            JavaSource specification = new JavaSource(method.getSourceCode());
                            sectionsInTestMethod.add(new Section(new JavaSource(methodCall.toString()), specification, true));
                        } else {
                            if (inATestWithCollapsibleMethods(sectionsInTestMethod)) {
                                sectionsInTestMethod.add(new Section(new JavaSource("Non collapsible method found, please annotate with collapsible annotation"), false));
                            }
                        }
                    }
                }
            }
        }
        return sectionsInTestMethod;
    }

    private boolean inATestWithCollapsibleMethods(List<Section> sectionsInTestMethod) {
        return !sectionsInTestMethod.isEmpty();
    }

    private Predicate<? super JavaMethod> methodName2(final String name) {
        return new Predicate<JavaMethod>() {
            @Override
            public boolean matches(JavaMethod other) {
                return other.getName().equals(name);
            }
        };
    }

    private Predicate<? super JavaMethod> argumentTypes2(final List<Expression> args) {
        return new Predicate<JavaMethod>() {
            @Override
            public boolean matches(JavaMethod other) {
                if (args.size() != other.getParameters().length) {
                    return false;
                }
                for (int i = 0; i < other.getParameters().length; i++) {
                    JavaParameter parameter = other.getParameters()[i];
                    Expression expectedParameter = args.get(i);
                    if (expectedParameter.getClass().equals(IntegerLiteralExpr.class) && !(parameter.getType().getFullyQualifiedName().equals(Integer.class.getName()) || parameter.getType().getFullyQualifiedName().equals(int.class.getName()))) {
                        return false;
                    }
                    if (expectedParameter.getClass().equals(StringLiteralExpr.class) && !parameter.getType().getFullyQualifiedName().equals(String.class.getName())) {
                        return false;
                    }
                }
                // TODO improve @Collapsible: other possible types of Expression
                return true;
            }
        };
    }

    private Predicate<? super Method> argumentTypes(final List<Expression> expectedParameterTypes) {
        return new Predicate<Method>() {
            @Override
            public boolean matches(Method other) {
                Class<?>[] parameterTypes = other.getParameterTypes();
                for (int i = 0; i < expectedParameterTypes.size(); i++) {
                    Expression expression = expectedParameterTypes.get(i);
                    if (expression.getClass().equals(IntegerLiteralExpr.class) && !(parameterTypes[i].equals(Integer.class) || parameterTypes[i].equals(int.class))) {
                        return false;
                    }
                    if (expression.getClass().equals(StringLiteralExpr.class) && !parameterTypes[i].equals(String.class)) {
                        return false;
                    }
                    if (expression.getClass().equals(NameExpr.class) && !parameterTypes[i].equals(String.class)) {
                        return false;
                    }
                    // TODO improve @Collapsible: implement other possible types of Expression
                }
                return true;
            }
        };
    }

    private Predicate<? super Method> numberOfArguments(final int requiredNumberOfArguments) {
        return new Predicate<Method>() {
            @Override
            public boolean matches(Method other) {
                return other.getParameterTypes().length == requiredNumberOfArguments;
            }
        };
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
