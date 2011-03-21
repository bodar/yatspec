package com.googlecode.yatspec.rendering;

public interface Renderer<T> {
    String render(T t) throws Exception;
}
