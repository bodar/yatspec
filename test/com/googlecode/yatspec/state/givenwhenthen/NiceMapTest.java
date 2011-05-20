package com.googlecode.yatspec.state.givenwhenthen;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
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

    @Test
    public void shouldAddOneMapToAnother() throws Exception {
        NiceMap niceMap = new NiceMap().add("Key", "value");
        NiceMap anotherNiceMap = new NiceMap().add("Key2", "value2");

        Map<String, String> combinedMap = niceMap.putAll(anotherNiceMap).getTypes();
        
        assertThat(combinedMap, hasEntry("Key", "value"));
    }

}
