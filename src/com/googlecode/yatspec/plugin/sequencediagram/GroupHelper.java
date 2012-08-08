package com.googlecode.yatspec.plugin.sequencediagram;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class GroupHelper {
    public static final Pattern PATTERN = Pattern.compile(">\\((.*)\\).*");
    private boolean hasGroup;

    public String markupGroup(String currentLine) {
        Matcher matcher = PATTERN.matcher(currentLine);
        boolean foundGroup = matcher.find();
        if(foundGroup && !hasGroup) {
            hasGroup = true;
            return format("group %s%n", matcher.group(1));
        } else if (!foundGroup && hasGroup) {
            hasGroup = false;
            return format("end%n");
        }
        return "";
    }

    public String cleanUpOpenGroups() {
        return hasGroup ? format("end%n") : "";
    }
}
