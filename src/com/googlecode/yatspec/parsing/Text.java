package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.yatspec.regex.Matches;
import org.apache.commons.lang.StringUtils;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Text {
    private static final Pattern wordDelimiter = Pattern.compile("[\\\\(\\\\),\\\\.;]|([A-Z]*?)([A-Z][a-z])");
    private static final Pattern spaceRemover = Pattern.compile("\\s*(\\S+\\s)");
    private static final Pattern stringIgnorer = Pattern.compile("\"[^\"]+\"");

    private static final Callable1<MatchResult, String> wordDelimiterReplacer = new Callable1<MatchResult, String>() {
        public String call(MatchResult matchResult) {
            // " $1 $2"
            return " " + lowercaseSingleLetters(Matches.filterNull(matchResult.group(1))) + " " + Matches.filterNull(matchResult.group(2)).toLowerCase();
        }
    };

    private static final Callable1<String, String> wordifier = new Callable1<String, String>() {
        public String call(String text) {
            return Matches.matches(wordDelimiter, text).replace(wordDelimiterReplacer);
        }
    };

    private static final Callable1<MatchResult, String> doNothing = new Callable1<MatchResult, String>()  {
        public String call(MatchResult matchResult) {
            return matchResult.group();
        }
    };

    private static String lowercaseSingleLetters(String value) {
        if( value.length() == 1){
            return value.toLowerCase();
        }
        return value;
    }

    private static final String spaceRemoverReplacer = "$1";


    public static String wordify(String value) {
        final String wordified = Matches.matches(stringIgnorer, value).replace(wordifier, doNothing);
        return StringUtils.capitalize(spaceRemover.matcher(wordified).replaceAll(spaceRemoverReplacer));
    }
   
}
