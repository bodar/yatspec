package com.googlecode.yatspec.junit;

import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;

@RunWith(SpecRunner.class)
public class BeforeClassTest extends TestState {

    public static final List<String> list = new ArrayList<String>();
    public final String item1 = theFirstItemInTheList();

    @BeforeClass
    public static void addItemsToList() {
        list.add("item1");
    }

    @Test
    public void checkFirstItem() {
        assertThat(theFirstItemInTheList(), is(item1));
    }

    public String theFirstItemInTheList() {
        return list.get(0);
    }
}