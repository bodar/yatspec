package com.googlecode.yatspec.rendering;

import java.io.File;
import java.io.IOException;
import static java.util.UUID.randomUUID;

public class TemporaryDirectory extends File {
    public TemporaryDirectory() throws IOException {
        super(System.getProperty("java.io.tmpdir"), randomUUID().toString());
        boolean succeeded = this.mkdir();
        if(!succeeded) throw new IOException("Could not create temp directory " + this.getAbsolutePath());
        deleteOnExit();
    }

}
