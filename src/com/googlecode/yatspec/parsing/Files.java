package com.googlecode.yatspec.parsing;

import com.googlecode.yatspec.parsing.Command;

import java.io.File;

public class Files {
    public static File find(String filename) {
        try {
            final String workingDirectory = System.getProperty("user.dir");
            final String absolutePath = Command.exec("find", workingDirectory, "-name", filename);
            return new File(absolutePath);
        } catch (Exception e) {
            System.err.println("Couldn't find file " + filename);
            throw new RuntimeException(e);
        }
    }
}
