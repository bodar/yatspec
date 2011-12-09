package com.googlecode.yatspec.fixture;

import java.util.Collection;
import java.util.Random;

public class RandomFixtures {

    private static final Random random = new Random();

    public static String anyString() {
        return anyStringOfLength(anyNumberBetween(8, 12));
    }

    public static String anyStringOfLength(int length) {
        String anyString = "";
        for (int index = 0; index < length; index++) {
            anyString += anyCapitalLetter();
        }
        return anyString;
    }

    public static String anyCapitalLetter() {
        return String.valueOf((char) (random.nextInt(26) + 'A'));
    }

    public static int anyNumberBetween(int inclusiveStart, int inclusiveEnd) {
        return random.nextInt(inclusiveEnd - inclusiveStart + 1) + inclusiveStart;
    }

    public static <T> T pickOneOf(T... choices) {
        return choices[random.nextInt(choices.length)];
    }

    public static <T> T pickOneOf(Collection<T> choices) {
        //noinspection unchecked
        return (T) pickOneOf(choices.toArray());
    }

    public static <E extends Enum> E pickOneOf(Class<E> choices) {
        //noinspection unchecked
        return pickOneOf(choices.getEnumConstants());
    }
}
