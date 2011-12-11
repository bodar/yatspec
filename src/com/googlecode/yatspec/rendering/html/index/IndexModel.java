package com.googlecode.yatspec.rendering.html.index;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.*;
import com.googlecode.yatspec.rendering.Index;
import com.googlecode.yatspec.rendering.PackageNames;
import com.googlecode.yatspec.rendering.html.TestMethodPath;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Results;
import com.googlecode.yatspec.state.Status;
import com.googlecode.yatspec.state.TestMethod;

import java.io.File;

import static com.googlecode.funclate.Model.model;
import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.yatspec.parsing.Text.wordify;
import static com.googlecode.yatspec.rendering.PackageNames.allAncestors;
import static com.googlecode.yatspec.rendering.PackageNames.directSubpackageOf;
import static com.googlecode.yatspec.rendering.PackageNames.packageDisplayName;
import static com.googlecode.yatspec.state.Results.resultStatus;
import static com.googlecode.yatspec.state.StatusPriority.statusPriority;

public class IndexModel {
    private final Sequence<Pair<File, Result>> entries;
    private final Sequence<String> packageNames;

    public IndexModel(Index index) {
        this.entries = index.entries().memorise();
        this.packageNames = entries.
                map(second(Result.class)).
                map(Results.packageName()).
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

    private static Callable1<? super Pair<File, Result>, Model> modelOfResult() {
        return new Callable1<Pair<File, Result>, Model>() {
            @Override
            public Model call(Pair<File, Result> fileAndResult) throws Exception {
                Result result = fileAndResult.second();
                File file = fileAndResult.first();
                return model().
                        add("name", result.getName()).
                        add("url", "file://" + file).
                        add("status", some(result).map(resultStatus()).get()).
                        add("methods", sequence(result.getTestMethods()).
                                map(testMethodModel(file)).
                                toList());
            }
        };
    }

    private Status statusOfPackage(String name) throws Exception {
        return entries.
                filter(where(packageName(), startsWith(name))).
                map(second(Result.class)).
                map(resultStatus()).
                sortBy(statusPriority()).
                headOption().
                getOrElse(Status.Passed);
    }

    private static Callable1<? super TestMethod, Model> testMethodModel(final File file) {
        return new Callable1<TestMethod, Model>() {
            @Override
            public Model call(TestMethod testMethod) throws Exception {
                return model().
                        add("name", testMethod.getDisplayName()).
                        add("url", TestMethodPath.testMethodPath(testMethod, file)).
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

    private Callable1<? super Pair<File, Result>, String> packageName() {
        return new Callable1<Pair<File, Result>, String>() {
            @Override
            public String call(Pair<File, Result> result) throws Exception {
                return result.second().getPackageName();
            }
        };
    }
}
