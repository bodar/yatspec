package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Files;
import com.googlecode.yatspec.state.Result;

import java.io.File;

public class ResultWriter {
    private final File outputDirectory;
    private final ResultRenderer resultRenderer;

    public ResultWriter(File outputDirectory, ResultRenderer resultRenderer) {
        this.outputDirectory = outputDirectory;
        this.resultRenderer = resultRenderer;
    }

    public File write(Result result) throws Exception {
        final File output = resultRenderer.outputFile(outputDirectory, result.getTestClass());
        output.delete();
        output.getParentFile().mkdirs();
        String content = resultRenderer.render(result);
        Files.write(content.getBytes("UTF-8"), output);
        System.out.println("Yatspec output:\n" + output);
        return output;
    }

}
