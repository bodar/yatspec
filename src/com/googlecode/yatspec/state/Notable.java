package com.googlecode.yatspec.state;

import com.googlecode.totallylazy.Option;
import com.googlecode.yatspec.junit.Notes;

public interface Notable {
    Option<Notes> getNotes() throws Exception;
}
