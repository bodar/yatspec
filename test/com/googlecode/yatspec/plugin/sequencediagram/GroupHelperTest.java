package com.googlecode.yatspec.plugin.sequencediagram;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GroupHelperTest {
    private GroupHelper groupHelper = new GroupHelper();

    @Test
    public void ignoresNoGroup() throws Exception {
        String response = groupHelper.markupGroup("here ->> there:<text>basdjlakjds</text>");

        assertThat(response, is(""));
    }

    @Test
    public void marksGroupStart() throws Exception {
        String response = groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds</text>");
        
        assertThat(response, is(String.format("group theGroup%n")));
    }
    
    @Test
    public void ignoresIfGroupStarted() throws Exception {
        groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds1</text>");
        String response = groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds2</text>");

        assertThat(response, is(String.format("")));
    }

    @Test
    public void endsGroupInSequence() throws Exception {
        groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds1</text>");
        String response = groupHelper.markupGroup("here ->> there:basdjlakjds2");

        assertThat(response, is(String.format("end%n")));        
    }
    
    @Test
    public void cleansUpOpenGroups() throws Exception {
        groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds1</text>");
        String response = groupHelper.cleanUpOpenGroups();

        assertThat(response, is(String.format("end%n")));
    }

    @Test
    public void notCleanupWhenNoOpenGroups() throws Exception {
        groupHelper.markupGroup("here ->> there:<text>(theGroup) basdjlakjds1</text>");
        groupHelper.markupGroup("here ->> there:<text>basdjlakjds2</text>");
        String response = groupHelper.cleanUpOpenGroups();

        assertThat(response, is(String.format("")));
    }
}
