package com.googlecode.yatspec.parsing;

import java.io.File;

public class Files {
    public static File find(String filename) {
        try {
            final String absolutePath = executeFindCommand(filename);
            return new File(absolutePath);
        } catch (Exception e) {
            System.err.println("Couldn't find file " + filename);
            throw new RuntimeException(e);
        }
    }
    public static boolean canFind(String filename) {
        try {
            return executeFindCommand(filename) != null;
        } catch (Exception ignore) {
            return false;
        }
    }

    private static String executeFindCommand(String filename) throws Exception {
        final String workingDirectory = System.getProperty("user.dir");
        return Command.exec("find", workingDirectory, "-name", filename);
    }
}
