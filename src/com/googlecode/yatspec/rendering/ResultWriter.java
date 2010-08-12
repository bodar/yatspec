package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.Result;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.googlecode.yatspec.parsing.TestParser.getPath;

public class ResultWriter {
    private final File outputDirectory;

    public ResultWriter(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void render(Result result) throws Exception {
        final File htmlOutput = getHtmlOutputFile(result.getTestClass());
        htmlOutput.delete();
        createAllDirectoriesNeededFor(htmlOutput);
        final FileWriter writer = new FileWriter(htmlOutput);
        String html = new ResultRenderer().render(result);
        writer.write(html);
        writer.flush();
        System.out.println("Html output:\n" + htmlOutput);
    }

    private File getHtmlOutputFile(Class testClass) {
        return new File(outputDirectory, getPath(testClass.getName()) + ".html");
    }

    private void createAllDirectoriesNeededFor(File file) throws IOException {
        final File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            FileUtils.forceMkdir(parentFile);
        }
    }
}
