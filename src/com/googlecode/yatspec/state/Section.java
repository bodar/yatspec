package com.googlecode.yatspec.state;

import com.googlecode.yatspec.parsing.JavaSource;
import com.sun.istack.internal.Nullable;

@SuppressWarnings("unused")
public class Section {
    private final JavaSource header;
    private final boolean collapsible;
    private final JavaSource specification;

    public Section(@Nullable JavaSource header, JavaSource specification, boolean collapsible) {
        this.header = header;
        this.specification = specification;
        this.collapsible = collapsible;
    }

    public Section(JavaSource specification, boolean collapsible) {
        this(null, specification, collapsible);
    }

    public JavaSource getHeader() {
        return header;
    }

    public boolean isCollapsible() {
        return collapsible;
    }

    public JavaSource getSpecification() {
        return specification;
    }
}
