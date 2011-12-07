package com.googlecode.yatspec.state;

import org.junit.Test;

import static com.googlecode.yatspec.state.StatusPriority.statusPriority;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class StatusPriorityTest {
    @Test
    public void ordersStatusesAccordingToImportance() throws Exception {
        assertThat(statusPriority().compare(Status.Passed, Status.Passed), is(0));

        assertThat(statusPriority().compare(Status.Passed, Status.Failed), greaterThan(0));
        assertThat(statusPriority().compare(Status.Passed, Status.NotRun), greaterThan(0));

        assertThat(statusPriority().compare(Status.Failed, Status.Passed), lessThan(0));
        assertThat(statusPriority().compare(Status.Failed, Status.NotRun), lessThan(0));

        assertThat(statusPriority().compare(Status.NotRun, Status.Passed), lessThan(0));
        assertThat(statusPriority().compare(Status.NotRun, Status.Failed), greaterThan(0));
    }
}
