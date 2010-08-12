package com.googlecode.yatspec.state.givenwhenthen;

import jedi.option.Option;

import java.util.LinkedHashMap;

import static jedi.option.Options.option;

public abstract class NiceMap<T extends NiceMap> extends LinkedHashMap<String, Object> {
    public NiceMap(Object... instances) {
        for (Object instance : instances) {
            add(instance);
        }
    }

    public <T> T getType(String key, Class<T> aClass) {
        return (T) get(key);
    }

    public <T> T getType(Class<T> aClass) {
        return getType(aClass.getSimpleName(),aClass);
    }

    public <T> Option<T> getOption(String key, Class<T> aClass) {
        return option(getType(key, aClass));
    }

    public <T> Option<T> getOption(Class<T> aClass) {
        return option(getType(aClass));
    }

    public T add(String key, Object instance){
        put(key, instance);
        return (T) this;
    }

    public T add(Object instance){
        if(instance == null) {
            return (T) this;
        }
        return add(instance.getClass().getSimpleName(), instance);
    }

    public boolean contains(Class aClass){
        return containsKey(aClass.getSimpleName());
    }

}
