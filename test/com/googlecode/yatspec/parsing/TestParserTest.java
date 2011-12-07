package com.googlecode.yatspec.parsing;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.TestMethod;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.TestParser.parseTestMethods;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;

@RunWith(SpecRunner.class)
public class TestParserTest {
    @Test
    @Notes("Some method notes")
    public void testParseTestMethods() throws Exception {
        final List<TestMethod> methods = parseTestMethods(getClass());
        assertThat(sequence(methods).first().getNotes().get().value(), is("Some method notes"));

    }

    private static final Object thisShouldntInterfereWithTheBelowTableTest = null;

    @Test
    @Table({
            @Row({"meh"})
    })
    public void yatspecWillTrimWhitespaceLeftBehindByQDoxInTableTestAnnotationsWhenAFieldVariableIsDeclared(String something) throws Exception {
        //A workaround for weirdness in QDox
    }

    @Test
    public void shouldParseTestMethodsFromClassFoundInClassPathRatherThanFileSystem() throws Exception {
        assertExistsInClassLoader("com/googlecode/yatspec/parsing/test/TestSource.java", "TestParserTest-sources.jar");
        assertExistsInClassLoader("com/googlecode/yatspec/parsing/test/TestSource.class", "TestParserTest.jar");

        List<TestMethod> testMethods = parseTestMethods(Class.forName("com.googlecode.yatspec.parsing.test.TestSource"));
        assertThat(sequence(testMethods).first().getName(), equalTo("testParseMethodWithSrcJar"));
    }

    private static void assertExistsInClassLoader(String resource, String location) {
        assertNotNull(format("Resource '%s' not found, check location '%s' is in classpath?", resource, location), Thread.currentThread().getContextClassLoader().getResource(resource));
    }

}
