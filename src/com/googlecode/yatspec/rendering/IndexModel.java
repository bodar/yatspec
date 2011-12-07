package com.googlecode.yatspec.rendering;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.*;
import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.state.Result;
import com.googlecode.yatspec.state.Results;
import com.googlecode.yatspec.state.Status;
import com.googlecode.yatspec.state.TestMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.googlecode.funclate.Model.model;
import static com.googlecode.totallylazy.Arrays.list;
import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.returnArgument;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Maps.asMultiValuedMap;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.totallylazy.regex.Regex.regex;
import static com.googlecode.yatspec.parsing.Text.wordify;
import static com.googlecode.yatspec.rendering.html.HtmlResultRenderer.getCssMap;
import static com.googlecode.yatspec.state.Results.resultStatus;
import static com.googlecode.yatspec.state.Results.testMethods;
import static com.googlecode.yatspec.state.StatusPriority.statusPriority;
import static java.lang.String.format;
import static java.util.regex.Pattern.quote;

public class IndexModel {
    private final Sequence<Pair<File, Result>> entries;
    private final Sequence<String> packageNames;

    public IndexModel(Index index) {
        this.entries = index.entries().memorise();
        this.packageNames = entries.
                map(second(Result.class)).
                map(Results.packageName()).
                unique().
                flatMap(allPackages()).
                unique();
    }

    private Callable1<? super String, ? extends Iterable<String>> allPackages() {
        return new Callable1<String, Iterable<String>>() {
            @Override
            public Iterable<String> call(String name) throws Exception {
                return sequence(name.split("\\.")).
                        fold(empty(String.class), buildPath());
            }

            private Callable2<? super Sequence<String>, ? super String, Sequence<String>> buildPath() {
                return new Callable2<Sequence<String>, String, Sequence<String>>() {
                    @Override
                    public Sequence<String> call(Sequence<String> allPaths, String name) throws Exception {
                        Option<String> longestPath = allPaths.
                                sortBy(returnArgument(String.class)).
                                lastOption();
                        if (longestPath.isEmpty())
                            return allPaths.add(name);
                        else
                            return allPaths.add(longestPath.get() + "." + name);
                    }
                };
            }
        };
    }

    public Model asModel() throws Exception {
        List<Pair<String, Model>> tags = new ArrayList<Pair<String, Model>>();
        for (Pair<File, Result> entry : entries) {
            for (TestMethod testMethod : entry.second().getTestMethods()) {
                Iterable<String> tagsForMethod = tagsFor(testMethod);
                for (String tag : tagsForMethod) {
                    tags.add(Pair.pair(tag, model().
                            add("package", entry.second().getPackageName()).
                            add("resultName", entry.second().getName()).
                            add("url", testMethodPath(testMethod, entry.first())).
                            add("class", getCssMap().get(testMethod.getStatus())).
                            add("name", testMethod.getDisplayName())));
                }
            }
        }
        Map<String, List<Model>> resultModels = sequence(tags).fold(new HashMap<String, List<Model>>(), asMultiValuedMap(String.class, Model.class));
        List<Model> tagModel = sequence(resultModels.keySet()).sortBy(returnArgument(String.class)).map(toTagModel(resultModels)).toList();
        List<Model> packageModels = modelOfPackage("").getValues("packages");
        return model().
                add("tags", tagModel).
                add("packages", packageModels);
    }

    private Callable1<? super String, Model> toTagModel(final Map<String, List<Model>> resultModels) {
        return new Callable1<String, Model>() {
            @Override
            public Model call(String tag) throws Exception {
                return model().
                        add("name", tag).
                        add("results", resultModels.get(tag));
            }
        };
    }

    private Iterable<String> tagsFor(TestMethod testMethod) {
        Notes notes = testMethod.getNotes();
        if(notes ==null) return empty();
        return regex("#[^ ]+").findMatches(notes.value()).map(new Callable1<MatchResult, String>() {
            @Override
            public String call(MatchResult matchResult) throws Exception {
                return matchResult.group();
            }
        });
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

    private String packageDisplayName(String name) {
        if ("".equals(name)) {
            return "/";
        } else if (name.lastIndexOf(".") >= 0) {
            return name.substring(name.lastIndexOf(".") + 1, name.length());
        } else {
            return name;
        }
    }

    private Callable1<? super Pair<File, Result>, Model> modelOfResult() {
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

    private Callable1<? super TestMethod, Model> testMethodModel(final File file) {
        return new Callable1<TestMethod, Model>() {
            @Override
            public Model call(TestMethod testMethod) throws Exception {
                return model().
                        add("name", testMethod.getDisplayName()).
                        add("url", testMethodPath(testMethod, file)).
                        add("status", testMethod.getStatus());
            }
        };
    }

    private static String testMethodPath(TestMethod testMethod, File resultFile) {
        return format("file://%s#%s", resultFile, testMethod.getName());
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

    private Predicate<? super String> directSubpackageOf(final String parentPackage) {
        return new Predicate<String>() {
            @Override
            public boolean matches(String packageName) {
                String pattern = format("%s\\.?[^.]+$", quote(parentPackage));
                return packageName.matches(pattern);
            }
        };
    }

    private boolean isSubpackage(boolean childPackagesOnly, String packageName, String parentPackage) {
        String pattern = format("%s\\.?[^.]+%s",
                quote(parentPackage),
                childPackagesOnly ? "$" : "");
        return packageName.matches(pattern);
    }
}
