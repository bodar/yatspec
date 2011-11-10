package com.googlecode.yatspec.rendering;

import com.googlecode.totallylazy.Files;
import com.googlecode.yatspec.state.Result;

import java.io.File;

public class ContentWriter<T> {
    private final File outputDirectory;
    private final ContentRenderer<T> contentRenderer;
    private boolean printFilename;

    public ContentWriter(File outputDirectory, ContentRenderer<T> contentRenderer, boolean printFilename) {
        this.outputDirectory = outputDirectory;
        this.contentRenderer = contentRenderer;
        this.printFilename = printFilename;
    }

    public File write(T instance) throws Exception {
        final File output = contentRenderer.outputFile(outputDirectory, instance);
        output.delete();
        output.getParentFile().mkdirs();
        String content = contentRenderer.render(instance);
        Files.write(content.getBytes("UTF-8"), output);
        if(printFilename){
            System.out.println("Yatspec output:\n" + output);
        }
        return output;
    }

}
