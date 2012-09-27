package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Status;
import com.googlecode.yatspec.state.TestMethod;

import java.io.File;

import static com.googlecode.funclate.Model.mutable.model;
import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.yatspec.parsing.Text.wordify;
import static com.googlecode.yatspec.rendering.PackageNames.*;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.htmlResultRelativePath;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.testMethodRelativePath;
import static com.googlecode.yatspec.state.Results.packageName;
import static com.googlecode.yatspec.state.Results.resultStatus;
import static com.googlecode.yatspec.state.StatusPriority.statusPriority;

public class IndexModel {
    private final Sequence<Result> entries;
    private final Sequence<String> packageNames;
    private final File yatspecOutputDir;

    public IndexModel(Index index, File yatspecOutputDir) {
        this.yatspecOutputDir = yatspecOutputDir;
        this.entries = index.entries().memorise();
        this.packageNames = entries.
                map(packageName()).
                unique().
                flatMap(allAncestors()).
                unique();
    }

    public Model asModel() throws Exception {
        return model().add("packages", modelOfPackage("").getValues("packages"));
    }

    private Model modelOfPackage(String name) throws Exception {
        return model().
                add("name", wordify(packageDisplayName(name))).
                add("status", statusOfPackage(name)).
                add("packages", packageNames.
                        filter(directSubpackageOf(name)).
                        sortBy(returnArgument(String.class)).
                        map(toPackageModel()).
                        toList()).
                add("results", entries.
                        filter(where(packageName(), is(name))).
                        map(modelOfResult()).
                        toList());
    }

    private Callable1<? super Result, Model> modelOfResult() {
        return new Callable1<Result, Model>() {
            @Override
            public Model call(Result result) throws Exception {
                return model().
                        add("name", result.getName()).
                        add("url", htmlResultRelativePath(result.getTestClass())).
                        add("status", some(result).map(resultStatus()).get()).
                        add("methods", sequence(result.getTestMethods()).
                                map(testMethodModel()).
                                toList());
            }
        };
    }

    private Status statusOfPackage(String name) throws Exception {
        return entries.
                filter(where(packageName(), startsWith(name))).
                map(resultStatus()).
                sortBy(statusPriority()).
                headOption().
                getOrElse(Status.Passed);
    }

    private Callable1<? super TestMethod, Model> testMethodModel() {
        return new Callable1<TestMethod, Model>() {
            @Override
            public Model call(TestMethod testMethod) throws Exception {
                return model().
                        add("name", testMethod.getDisplayName()).
                        add("url", testMethodRelativePath(testMethod)).
                        add("status", testMethod.getStatus());
            }
        };
    }

    private Callable1<? super String, Model> toPackageModel() {
        return new Callable1<String, Model>() {
            @Override
            public Model call(String packageName) throws Exception {
                return modelOfPackage(packageName);
            }
        };
    }
}
