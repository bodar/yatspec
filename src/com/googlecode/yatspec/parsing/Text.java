package com.googlecode.yatspec.parsing;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.regex.Regex;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.googlecode.totallylazy.regex.Regex.regex;

public class Text {
    private static final Regex wordDelimiter = Regex.regex(Strings.toString(Text.class.getResourceAsStream("wordDelimiter.regex")));
    private static final Pattern spaceRemover = Pattern.compile("(?<!^)[\\t\\x0B\\f ]+"); // don't replace new lines
    private static final Regex quotedStrings = regex("\"[^\"]+\"");

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
            return wordDelimiter.findMatches(text).replace(wordDelimiterReplacer);
        }
    };

    private static final Callable1<MatchResult, CharSequence> doNothing = new Callable1<MatchResult, CharSequence>()  {
        public CharSequence call(MatchResult matchResult) {
            return matchResult.group();
        }
    };

    private static String lowercaseSingleLetters(String value) {
        return value.length() == 1 ? value.toLowerCase() : value;
    }


    public static String wordify(String value) {
        final String wordified = quotedStrings.findMatches(value).replace(wordifier, doNothing);
        return Strings.capitalise(spaceRemover.matcher(wordified).replaceAll(" ").trim());
    }

    public static Callable1<String, String> wordify() {
        return new Callable1<String, String>() {
            @Override
            public String call(String value) throws Exception {
                return wordify(value);
            }
        };
    }
}