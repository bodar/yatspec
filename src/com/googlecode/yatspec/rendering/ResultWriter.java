package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.Result;

import java.io.*;

import static com.googlecode.yatspec.parsing.Files.toHtmlPath;

public class ResultWriter {
    private final File outputDirectory;

    public ResultWriter(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public File write(Result result) throws Exception {
        final File htmlOutput = htmlOutputFile(result.getTestClass());
        htmlOutput.delete();
        htmlOutput.getParentFile().mkdirs();
        String content = new ResultRenderer().render(result);
        write(htmlOutput, content);
        System.out.println("Html output:\n" + htmlOutput);
        return htmlOutput;
    }

    private static void write(File file, String content) throws IOException {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
        } finally {
            closeQuietly(writer);
        }
    }

    private static void closeQuietly(Writer writer) throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    private File htmlOutputFile(Class testClass) {
        return new File(outputDirectory, toHtmlPath(testClass));
    }
}
