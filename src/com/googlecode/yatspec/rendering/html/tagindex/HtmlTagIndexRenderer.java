package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.funclate.Model;
import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Group;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Results;
import com.googlecode.yatspec.state.TestMethod;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.yatspec.parsing.Files.overwrite;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.testMethodRelativePath;

public class HtmlTagIndexRenderer implements SpecResultListener {
    public static final String TAG_NAME = "tag";
    private final static Index index = new Index();
    private final TagFinder tagFinder;

    public HtmlTagIndexRenderer() {
        this(new NotesTagFinder());
    }

    public HtmlTagIndexRenderer(TagFinder tagFinder) {
        this.tagFinder = tagFinder;
    }

    @Override
    public void complete(File yatspecOutputDir, Result result) throws Exception {
        index.add(result);
        overwrite(outputFile(yatspecOutputDir), render(index));
    }

    public String render(Index index) throws Exception {
        EnhancedStringTemplateGroup group = new EnhancedStringTemplateGroup(getClass());
        StringTemplate template = group.getInstanceOf("index",
                model().
                        add("script", null).
                        add("stylesheet", HtmlResultRenderer.loadContent("yatspec.css")).
                        add("cssClass", getCssMap()).
                        add("tags", tagModels(index).toList()).
                        toMap());
        return template.toString();
    }

    private Sequence<Model> tagModels(Index index) {
        return index.
                entries().
                flatMap(testMethods()).
                flatMap(methodTags()).
                groupBy(first(String.class)).
                sortBy(groupKey(String.class)).
                map(toTagModel());
    }

    private Callable1<? super TestMethod, ? extends Iterable<Pair<String, TestMethod>>> methodTags() {
        return new Callable1<TestMethod, Iterable<Pair<String, TestMethod>>>() {
            @Override
            public Iterable<Pair<String, TestMethod>> call(TestMethod resultFileAndTestMethod) throws Exception {
                return sequence(tagFinder.tags(resultFileAndTestMethod)).zip(repeat(resultFileAndTestMethod));
            }
        };
    }

    private static <K> Callable1<Group<K, ?>, K> groupKey(Class<K> keyType) {
        return new Callable1<Group<K, ?>, K>() {
            @Override
            public K call(Group<K, ?> group) throws Exception {
                return group.key();
            }
        };
    }

    private static Callable1<Pair<String, TestMethod>, Model> tagModel() {
        return new Callable1<Pair<String, TestMethod>, Model>() {
            @Override
            public Model call(Pair<String, TestMethod> tagAndTestMethod) throws Exception {
                TestMethod testMethod = tagAndTestMethod.second();
                return model().
                        add(TAG_NAME, tagAndTestMethod.first()).
                        add("package", testMethod.getPackageName()).
                        add("resultName", testMethod.getName()).
                        add("url", testMethodRelativePath(testMethod)).
                        add("class", getCssMap().get(testMethod.getStatus())).
                        add("name", testMethod.getDisplayName());
            }
        };
    }

    private static Callable1<? super Result, ? extends Iterable<TestMethod>> testMethods() {
        return new Callable1<Result, Iterable<TestMethod>>() {
            @Override
            public Iterable<TestMethod> call(Result fileResult) throws Exception {
                return sequence(fileResult).flatMap(Results.testMethods());
            }
        };
    }

    private static Callable1<Group<String, Pair<String, TestMethod>>, Model> toTagModel() {
        return new Callable1<Group<String, Pair<String, TestMethod>>, Model>() {
            @Override
            public Model call(Group<String, Pair<String, TestMethod>> tagGroup) throws Exception {
                return model().
                        add("name", tagGroup.key()).
                        add("results", tagGroup.map(tagModel()).toList());
            }
        };
    }

    private static File outputFile(File outputDirectory) {
        return new File(outputDirectory, "tag-index.html");
    }
}
