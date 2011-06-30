package com.googlecode.yatspec.state.givenwhenthen;

import java.util.concurrent.Callable;

public final class InterestingGivensTypeLoader<T> implements Callable<T> {
    private final String key;
    private final InterestingGivens interestingGivens;
    private final Class<T> klazz;

    private InterestingGivensTypeLoader(InterestingGivens interestingGivens, String key, Class<T> klazz) {
        this.interestingGivens = interestingGivens;
        this.klazz = klazz;
        this.key = key;
    }

    public T call() throws Exception {
        T type = key == null ? interestingGivens.getType(klazz) : interestingGivens.getType(key, klazz);
        checkThatTypeIsNotNull(type, key, klazz);
        return type;
    }

    private static <T> void checkThatTypeIsNotNull(T type, String key, Class<T> klazz) {
        if (type == null) {
            throw new IllegalStateException(String.format("Type returned is null, key=%s, class=%s", key, klazz));
        }
    }

    public static <T> Callable<T> interestingGivensType(InterestingGivens interestingGivens, Class<T> klazz) {
        return new InterestingGivensTypeLoader<T>(interestingGivens, null, klazz);
    }

    public static <T> Callable<T> interestingGivensType(InterestingGivens interestingGivens, String key, Class<T> klazz) {
        return new InterestingGivensTypeLoader<T>(interestingGivens, key, klazz);
    }
}
