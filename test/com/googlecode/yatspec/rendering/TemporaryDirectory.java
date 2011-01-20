package com.googlecode.yatspec.rendering;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class TemporaryDirectory extends File {
    public TemporaryDirectory() throws IOException {
        super(System.getProperty("java.io.tmpdir"), randomUUID().toString());
        FileUtils.forceMkdir(this);
        deleteOnExit();
    }

}
