package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Files;
import com.googlecode.yatspec.state.Result;

import java.io.*;

import static com.googlecode.yatspec.parsing.Files.toHtmlPath;

public class ResultWriter {
    private final File outputDirectory;
    private final Renderer<Result> resultRenderer;

    public ResultWriter(File outputDirectory, Renderer<Result> resultRenderer) {
        this.outputDirectory = outputDirectory;
        this.resultRenderer = resultRenderer;
    }

    public File write(Result result) throws Exception {
        final File output = outputFile(result.getTestClass());
        output.delete();
        output.getParentFile().mkdirs();
        String content = resultRenderer.render(result);
        Files.write(content.getBytes("UTF-8"), output);
        System.out.println("Yatspec output:\n" + output);
        return output;
    }

    private File outputFile(Class testClass) {
        return new File(outputDirectory, toHtmlPath(testClass));
    }
}
