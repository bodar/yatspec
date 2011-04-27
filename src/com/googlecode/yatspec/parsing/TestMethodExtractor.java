package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.state.ScenarioTable;
import com.googlecode.yatspec.state.TestMethod;
import net.sourceforge.pmd.ast.*;
import org.jaxen.JaxenException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.lines;
import static java.util.Arrays.asList;

public class TestMethodExtractor implements Callable1<Pair<ASTMethodDeclaration,Method>, TestMethod> {
    public static TestMethodExtractor extractTestMethod(File file) throws IOException {
        final Sequence<String> lines = lines(file);
        return new TestMethodExtractor(lines.toArray(String.class));
    }

    private final String[] lines;

    public TestMethodExtractor(String[] lines) {
        this.lines = lines;
    }

    public TestMethod call(Pair<ASTMethodDeclaration,Method> pair) {
        ASTMethodDeclaration methodAST = pair.first();
        Method method = pair.second();
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
        ASTBlockStatement firstBlock = blocks.get(0);
        ASTBlockStatement lastBlock = blocks.get(blocks.size() - 1);
        return asList(lines).subList(firstBlock.getBeginLine() - 1, lastBlock.getEndLine());
    }

    @SuppressWarnings({"unchecked"})
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

    private <T extends SimpleNode> List<String> getValues(List<T> astLiterals) {
        return sequence(astLiterals).map(extractValue()).toList();
    }

    private <T extends SimpleNode> Callable1<T, String> extractValue() {
        return new Callable1<T, String>() {
            public String call(T simpleNode) {
                return getValue(simpleNode);
            }
        };
    }

    private String getValue(SimpleNode parameter) {
        final String line = lines[parameter.getBeginLine() -1];
        final String valueWithQuotes = line.substring(parameter.getBeginColumn() - 1, parameter.getEndColumn());
        return valueWithQuotes.replace("\"", "");
    }
}
