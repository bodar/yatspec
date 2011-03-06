package com.googlecode.yatspec.rendering;

import com.googlecode.yatspec.state.Result;

public interface Renderer<T> {
    String render(T t) throws Exception;
}
