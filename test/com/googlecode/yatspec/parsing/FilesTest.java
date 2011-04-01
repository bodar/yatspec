package com.googlecode.yatspec.parsing;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FilesTest {

    @Test
    public void supportsLongClassNames() throws Exception {
        String longClassName = Files.toJavaPath(EnormouslyLongClassNameToFacilitateTheTestingOfFilesEssentiallyToCheckThereIsNoMonkeyBusinessGoingOnWithTheToStringingOfASequenceIfYouDontKnowTheresADefaultLimitOnTheLengthAndYouNeedToMakeSureYourToStringIsntTruncated.class);

        assertThat(longClassName.length(), is(expectedClassName().length()));
    }

    private String expectedClassName() {
        return EnormouslyLongClassNameToFacilitateTheTestingOfFilesEssentiallyToCheckThereIsNoMonkeyBusinessGoingOnWithTheToStringingOfASequenceIfYouDontKnowTheresADefaultLimitOnTheLengthAndYouNeedToMakeSureYourToStringIsntTruncated.class.getName() + ".java";
    }

    private static class EnormouslyLongClassNameToFacilitateTheTestingOfFilesEssentiallyToCheckThereIsNoMonkeyBusinessGoingOnWithTheToStringingOfASequenceIfYouDontKnowTheresADefaultLimitOnTheLengthAndYouNeedToMakeSureYourToStringIsntTruncated {

    }
}
