package com.googlecode.yatspec.junit5;

import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ExtendWith(SpecListener.class)
public class BeforeClassTest extends TestState {

    public static final List<String> list = new ArrayList<String>();
    private final String item1 = theFirstItemInTheList();

    @BeforeAll
    static void addItemsToList() {
        list.add("item1");
    }

    @Test
    void checkFirstItem() {
        assertThat(theFirstItemInTheList(), is(item1));
    }

    private String theFirstItemInTheList() {
        return list.get(0);
    }
}