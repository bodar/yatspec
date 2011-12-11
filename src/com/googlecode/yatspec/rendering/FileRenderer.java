package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Files;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.state.Result;

import java.io.File;

public abstract class FileRenderer implements SpecResultListener {
    @Override
    public void complete(File yatspecOutputDir, Result result) throws Exception {
        final File output = outputFile(yatspecOutputDir, result);
        output.delete();
        output.getParentFile().mkdirs();
        String content = render(result);
        Files.write(content.getBytes("UTF-8"), output);
        System.out.println("Yatspec output:\n" + output);
    }

    protected abstract String render(Result result) throws Exception;
    protected abstract File outputFile(File outputDirectory, Result result);
}
