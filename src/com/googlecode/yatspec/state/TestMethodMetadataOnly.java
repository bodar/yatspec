package com.googlecode.yatspec.state;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TestMethodMetadataOnly implements TestMethodMetadata, Serializable {
    private final String name, displayName, displayLinkName, uid, packageName, testClassName;
    private final Status status;
    private final List<Annotation> annotations;

    public TestMethodMetadataOnly(String name, String displayName, String displayLinkName, String uid, String packageName, String testClassName, Status status, List<Annotation> annotations) {
        this.name = name;
        this.displayName = displayName;
        this.displayLinkName = displayLinkName;
        this.uid = uid;
        this.packageName = packageName;
        this.testClassName = testClassName;
        this.status = status;
        this.annotations = annotations;
    }

    public static TestMethodMetadataOnly fromTestMethod(TestMethodMetadata original) {
        return new TestMethodMetadataOnly(original.getName(),
                original.getDisplayName(),
                original.getDisplayLinkName(),
                original.getUid(),
                original.getPackageName(),
                original.getTestClassName(),
                original.getStatus(),
                Collections.unmodifiableList(new ArrayList<>(original.getAnnotations())));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDisplayLinkName() {
        return displayLinkName;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public String getTestClassName() {
        return testClassName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestMethodMetadataOnly that = (TestMethodMetadataOnly) o;
        return Objects.equals(name, that.name) && Objects.equals(displayName, that.displayName) && Objects.equals(displayLinkName, that.displayLinkName) && Objects.equals(uid, that.uid) && Objects.equals(packageName, that.packageName) && Objects.equals(testClassName, that.testClassName) && status == that.status && Objects.equals(annotations, that.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, displayName, displayLinkName, uid, packageName, testClassName, status, annotations);
    }

    @Override
    public String toString() {
        return "TestMethodMetadataOnly{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", displayLinkName='" + displayLinkName + '\'' +
                ", uid='" + uid + '\'' +
                ", packageName='" + packageName + '\'' +
                ", testClassName='" + testClassName + '\'' +
                ", status=" + status +
                ", annotations=" + annotations +
                '}';
    }
}
