package com.googlecode.yatspec.state.givenwhenthen;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

@SuppressWarnings({"unused", "unchecked"})
class NiceMap<T extends NiceMap> {
    private final Map<String, Object> map = Collections.synchronizedMap(new LinkedHashMap<String, Object>());

    public NiceMap(Object... instances) {
        for (Object instance : instances) {
            add(instance);
        }
    }

    public <R> R getType(String key, Class<R> aClass) {
        Object value = map.get(key);
        if(value == null) {
            return null;
        }
        if(!aClass.isAssignableFrom(value.getClass())){
            throw new ClassCastException("You requested a " + aClass.getSimpleName() + " but got a " + value.getClass() + " (" + value + ")");
        }
        return (R) value;
    }

    public Map<String, Object> getTypes() {
        synchronized (map) {
            return unmodifiableMap(new LinkedHashMap<String, Object>(map));
        }
    }

    public <R> R getType(Class<R> aClass) {
        return getType(defaultName(aClass), aClass);
    }

    public T add(String key, Object instance){
        map.put(key, instance);
        return (T) this;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public T add(Object instance){
        if(instance == null) {
            return (T) this;
        }
        return add(defaultName(instance.getClass()), instance);
    }

    public boolean contains(Class aClass){
        return contains(defaultName(aClass));
    }

    public boolean contains(String name) {
        return map.containsKey(name);
    }

    private static String defaultName(Class<?> aClass) {
        return aClass.getSimpleName();
    }

    public T  putAll(Iterable<Map.Entry<String, Object>> entries) {
        for (Map.Entry<String, Object> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return (T) this;
    }

    public void clear() {
        map.clear();
    }

    public void remove(String key) {
        map.remove(key);
    }
}
