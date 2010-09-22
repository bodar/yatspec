package com.googlecode.yatspec.state.givenwhenthen;

import jedi.option.Option;

import java.util.LinkedHashMap;

import static jedi.option.Options.option;

public class NiceMap<T extends NiceMap> extends LinkedHashMap<String, Object> {
    public NiceMap(Object... instances) {
        for (Object instance : instances) {
            add(instance);
        }
    }

    public <R> R getType(String key, Class<R> aClass) {
        Object value = get(key);
        if(value == null) {
            return null;
        }
        if(!aClass.isAssignableFrom(value.getClass())){
            throw new ClassCastException("You requested a " + aClass.getSimpleName() + " but got a " + value.getClass() + " (" + value + ")");
        }
        return (R) value;
    }

    public <R> R getType(Class<R> aClass) {
        return getType(aClass.getSimpleName(),aClass);
    }

    public <R> Option<R> getOption(String key, Class<R> aClass) {
        return option(getType(key, aClass));
    }

    public <R> Option<R> getOption(Class<R> aClass) {
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
