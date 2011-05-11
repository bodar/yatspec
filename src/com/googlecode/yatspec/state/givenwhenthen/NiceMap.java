package com.googlecode.yatspec.state.givenwhenthen;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

@SuppressWarnings("unused")
class NiceMap<T extends NiceMap> {
    private final Map<String, Object> map = new LinkedHashMap<String, Object>();

    public NiceMap(Object... instances) {
        for (Object instance : instances) {
            add(instance);
        }
    }

    @SuppressWarnings({"unchecked"})
    public final <R> R getType(String key, Class<R> aClass) {
        Object value = map.get(key);
        if(value == null) {
            return null;
        }
        if(!aClass.isAssignableFrom(value.getClass())){
            throw new ClassCastException("You requested a " + aClass.getSimpleName() + " but got a " + value.getClass() + " (" + value + ")");
        }
        return (R) value;
    }

    public final Map<String, Object> getTypes() {
        return unmodifiableMap(map);
    }

    public final <R> R getType(Class<R> aClass) {
        return getType(defaultName(aClass), aClass);
    }

    @SuppressWarnings({"unchecked"})
    public final T add(String key, Object instance){
        map.put(key, instance);
        return (T) this;
    }

    public final boolean isEmpty() {
        return map.isEmpty();
    }

    @SuppressWarnings({"unchecked"})
    public final T add(Object instance){
        if(instance == null) {
            return (T) this;
        }
        return add(defaultName(instance.getClass()), instance);
    }

    public final boolean contains(Class aClass){
        return contains(defaultName(aClass));
    }

    public final boolean contains(String name) {
        return map.containsKey(name);
    }

    private static String defaultName(Class<? extends Object> aClass) {
        return aClass.getSimpleName();
    }

}
