package com.googlecode.yatspec.parsing;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Command {
    public static String exec(String ... args) throws Exception {
        final Process process = new ProcessBuilder(args).start();
        process.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader.readLine();
    }
}
