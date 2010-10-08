package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.regex.Matches;
import com.googlecode.totallylazy.regex.Regex;
import org.apache.commons.lang.StringUtils;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.regex.Regex.regex;

public class Text {
    private static final Regex wordDelimiter = Regex.regex("[\\\\(\\\\),\\\\.;]|([A-Z]*?)([A-Z][a-z])");
    private static final Pattern spaceRemover = Pattern.compile("\\s*(\\S+\\s)");
    private static final Regex stringIgnorer = regex("\"[^\"]+\"");

    private static final Callable1<MatchResult, CharSequence> wordDelimiterReplacer = new Callable1<MatchResult, CharSequence>() {
        public String call(MatchResult matchResult) {
            // " $1 $2"
            return " " + lowercaseSingleLetters(filterNull(matchResult.group(1))) + " " + filterNull(matchResult.group(2)).toLowerCase();
        }
    };

    private static String filterNull(String value) {
        return value == null ? "" : value;
    }

    private static final Callable1<CharSequence, CharSequence> wordifier = new Callable1<CharSequence, CharSequence>() {
        public CharSequence call(CharSequence text) {
            return wordDelimiter.matches(text).replace(wordDelimiterReplacer);
        }
    };

    private static final Callable1<MatchResult, CharSequence> doNothing = new Callable1<MatchResult, CharSequence>()  {
        public CharSequence call(MatchResult matchResult) {
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
        final String wordified = stringIgnorer.matches(value).replace(wordifier, doNothing);
        return StringUtils.capitalize(spaceRemover.matcher(wordified).replaceAll(spaceRemoverReplacer));
    }
}