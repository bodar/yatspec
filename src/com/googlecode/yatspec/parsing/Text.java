package com.googlecode.yatspec.parsing;

import com.googlecode.yatspec.regex.Matches;
import jedi.functional.Functor;
import org.apache.commons.lang.StringUtils;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Text {
    private static final Pattern wordDelimiter = Pattern.compile("[\\\\(\\\\),\\\\.;]|([A-Z]*?)([A-Z][a-z])");
    private static final Pattern spaceRemover = Pattern.compile("\\s*(\\S+\\s)");
    private static final Pattern stringIgnorer = Pattern.compile("\"[^\"]+\"");

    private static final Functor<MatchResult, String> wordDelimiterReplacer = new Functor<MatchResult, String>() {
        public String execute(MatchResult matchResult) {
            // " $1 $2"
            return " " + lowercaseSingleLetters(Matches.filterNull(matchResult.group(1))) + " " + Matches.filterNull(matchResult.group(2)).toLowerCase();
        }
    };

    private static final Functor<String, String> wordifier = new Functor<String, String>() {
        public String execute(String text) {
            return Matches.matches(wordDelimiter, text).replace(wordDelimiterReplacer);
        }
    };

    private static final Functor<MatchResult, String> doNothing = new Functor<MatchResult, String>()  {
        public String execute(MatchResult matchResult) {
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
