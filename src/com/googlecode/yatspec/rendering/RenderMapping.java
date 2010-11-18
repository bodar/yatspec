package com.googlecode.yatspec.rendering;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RenderMapping {
    Class<?> type();
    Class<? extends Renderer<?>> renderer();
}
