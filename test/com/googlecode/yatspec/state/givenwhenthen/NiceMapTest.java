package com.googlecode.yatspec.state.givenwhenthen;

import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@SuppressWarnings("unchecked")
public class NiceMapTest {
    @Test
    public void shouldGiveAUsefulMessageIfTheWrongClassIsRequested() throws Exception {
        NiceMap map = new NiceMap("Moomin");
        try {
            map.getType("String", Integer.class);
            fail("Expected exception");
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Moomin"));
        }
    }

}
