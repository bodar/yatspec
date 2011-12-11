package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.funclate.Model;
import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Group;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.rendering.ContentRenderer;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Results;
import com.googlecode.yatspec.state.TestMethod;
import org.antlr.stringtemplate.StringTemplate;

import java.io.File;
import java.util.regex.MatchResult;

import static com.googlecode.funclate.Model.model;
import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Sequences.*;
import static com.googlecode.totallylazy.regex.Regex.regex;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;
import static com.googlecode.yatspec.rendering.html.TestMethodPath.testMethodPath;

public class HtmlTagIndexRenderer implements ContentRenderer<Index> {

    public static final String TAG_NAME = "tag";

    @Override
    public File outputFile(File outputDirectory, Index instance) {
        return new File(outputDirectory, "tag-index.html");
    }

    @Override
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

    private static Sequence<Model> tagModels(Index index) {
        return index.
                entries().
                flatMap(testMethods()).
                flatMap(methodTags()).
                groupBy(first(String.class)).
                sortBy(groupKey(String.class)).
                map(toTagModel());
    }

    private static Callable1<? super Pair<File, TestMethod>, ? extends Iterable<Pair<String, Pair<File, TestMethod>>>> methodTags() {
        return new Callable1<Pair<File, TestMethod>, Iterable<Pair<String, Pair<File, TestMethod>>>>() {
            @Override
            public Iterable<Pair<String, Pair<File, TestMethod>>> call(Pair<File, TestMethod> resultFileAndTestMethod) throws Exception {
                return tagsFor(resultFileAndTestMethod.second()).zip(repeat(resultFileAndTestMethod));
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

    private static Callable1<Pair<String, Pair<File, TestMethod>>, Model> tagModel() {
        return new Callable1<Pair<String, Pair<File, TestMethod>>, Model>() {
            @Override
            public Model call(Pair<String, Pair<File, TestMethod>> tagAndTestMethod) throws Exception {
                TestMethod testMethod = tagAndTestMethod.second().second();
                File resultOutputFile = tagAndTestMethod.second().first();
                return model().
                        add(TAG_NAME, tagAndTestMethod.first()).
                        add("package", testMethod.getPackageName()).
                        add("resultName", testMethod.getName()).
                        add("url", testMethodPath(testMethod, resultOutputFile)).
                        add("class", getCssMap().get(testMethod.getStatus())).
                        add("name", testMethod.getDisplayName());
            }
        };
    }

    private static Callable1<? super Pair<File, Result>, ? extends Iterable<Pair<File, TestMethod>>> testMethods() {
        return new Callable1<Pair<File, Result>, Iterable<Pair<File, TestMethod>>>() {
            @Override
            public Iterable<Pair<File, TestMethod>> call(Pair<File, Result> fileResult) throws Exception {
                return repeat(fileResult.first()).zip(sequence(fileResult.second()).flatMap(Results.testMethods()));
            }
        };
    }

    private static Callable1<Group<String, Pair<String, Pair<File, TestMethod>>>, Model> toTagModel() {
        return new Callable1<Group<String, Pair<String, Pair<File, TestMethod>>>, Model>() {
            @Override
            public Model call(Group<String, Pair<String, Pair<File, TestMethod>>> tagGroup) throws Exception {
                return model().
                        add("name", tagGroup.key()).
                        add("results", tagGroup.map(tagModel()).toList());
            }
        };
    }

    private static Sequence<String> tagsFor(TestMethod testMethod) {
        Notes notes = testMethod.getNotes();
        if (notes == null) return empty();
        return regex("#[^ ]+").findMatches(notes.value()).map(new Callable1<MatchResult, String>() {
            @Override
            public String call(MatchResult matchResult) throws Exception {
                return matchResult.group();
            }
        });
    }
}
