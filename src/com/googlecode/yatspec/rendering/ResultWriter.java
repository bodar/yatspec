package com.googlecode.yatspec.rendering;

import static com.googlecode.yatspec.parsing.TestParser.getPath;
import com.googlecode.yatspec.state.Result;

import java.io.File;
import java.io.FileWriter;

public class ResultWriter {
    private final File outputDirectory;

    public ResultWriter(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public File write(Result result) throws Exception {
        final File htmlOutput = getHtmlOutputFile(result.getTestClass());
        htmlOutput.delete();
        htmlOutput.getParentFile().mkdirs();
        final FileWriter writer = new FileWriter(htmlOutput);
        String html = new ResultRenderer().render(result);
        writer.write(html);
        writer.flush();
        System.out.println("Html output:\n" + htmlOutput);
        return htmlOutput;
    }

    private File getHtmlOutputFile(Class testClass) {
        return new File(outputDirectory, getPath(testClass.getName()) + ".html");
    }
}
