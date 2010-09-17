package com.googlecode.yatspec.parsing;

import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import jedi.functional.Functor;
import net.sourceforge.pmd.ast.*;
import org.jaxen.JaxenException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static jedi.functional.FunctionalPrimitives.collect;
import static jedi.functional.FunctionalPrimitives.first;
import static jedi.functional.FunctionalPrimitives.last;

public class TestMethodExtractor implements Functor<List, TestMethod> {
    public static TestMethodExtractor extractTestMethod(String wholeFile) throws IOException {
        final String[] lines = wholeFile.split(TestParser.LINE_SEPARATOR);
        return new TestMethodExtractor(lines);
    }

    private final String[] lines;

    public TestMethodExtractor(String[] lines) {
        this.lines = lines;
    }

    public TestMethod execute(List pair) {
        ASTMethodDeclaration methodAST = (ASTMethodDeclaration) pair.get(0);
        Method method = (Method) pair.get(1);
        final List<ASTBlockStatement> blocks = methodAST.findChildrenOfType(ASTBlockStatement.class);
        if (blocks.isEmpty()) {
            return null;
        }

        final String name = methodAST.getMethodName();
        final List<String> source = getSourceForBlock(blocks);
        final ScenarioTable scenarioTable = getScenarioTable(methodAST);
        return new TestMethod(method, name, source, scenarioTable);
    }

    private List<String> getSourceForBlock(List<ASTBlockStatement> blocks) {
        ASTBlockStatement firstBlock = first(blocks);
        ASTBlockStatement lastBlock = last(blocks);
        return Arrays.asList(lines).subList(firstBlock.getBeginLine() - 1, lastBlock.getEndLine());
    }

    private ScenarioTable getScenarioTable(ASTMethodDeclaration method) {
        ScenarioTable table = new ScenarioTable();
        try {
            final ASTMethodDeclarator declarator = method.getFirstChildOfType(ASTMethodDeclarator.class);
            final List<ASTVariableDeclaratorId> declaratorIds = declarator.findChildrenOfType(ASTVariableDeclaratorId.class);
            final List<String> parameters = getValues(declaratorIds);
            table.setHeaders(parameters);

            final List<ASTSingleMemberAnnotation> rows = method.findChildNodesWithXPath("preceding-sibling::Annotation/SingleMemberAnnotation[Name/@Image='Table']//Annotation/SingleMemberAnnotation[Name/@Image='Row']");
            for (ASTSingleMemberAnnotation row : rows) {
                final List<ASTLiteral> astLiterals = row.findChildNodesWithXPath("descendant::PrimaryExpression/descendant::Literal | descendant::PrimaryExpression/descendant::Name");
                final List<String> values = getValues(astLiterals);
                table.addRow(values);
            }
        } catch (JaxenException e) {
            throw new RuntimeException(e);
        }
        return table;
    }

    private <T extends SimpleNode>List<String> getValues(List<T> astLiterals) {
        return collect(astLiterals, new Functor<T, String>() {
            public String execute(T simpleNode) {
                return getValue(simpleNode);
            }
        });
    }

    private String getValue(SimpleNode parameter) {
        final String line = lines[parameter.getBeginLine() -1];
        final String valueWithQuotes = line.substring(parameter.getBeginColumn() - 1, parameter.getEndColumn());
        return valueWithQuotes.replace("\"", "");
    }
}
