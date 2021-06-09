package com.googlecode.yatspec.state;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ResultMetadataOnly implements ResultMetadata, Serializable {
    private final String name, packageName, testClassName;
    private final List<TestMethodMetadata> testMethodMetadata;
    private final List<Annotation> annotations;

    public ResultMetadataOnly(String name, String packageName, String testClassName, List<TestMethodMetadata> testMethodMetadata, List<Annotation> annotations) {
        this.name = name;
        this.packageName = packageName;
        this.testClassName = testClassName;
        this.testMethodMetadata = testMethodMetadata;
        this.annotations = annotations;
    }

    public static ResultMetadataOnly fromResult(ResultMetadata original) {
        ArrayList<TestMethodMetadata> methodMetadata = new ArrayList<>();
        for (TestMethodMetadata originalMethod : original.getTestMethodMetadata()) {
            methodMetadata.add(TestMethodMetadataOnly.fromTestMethod(originalMethod));
        }
        return new ResultMetadataOnly(original.getName(),
                original.getPackageName(),
                original.getTestClassName(),
                Collections.unmodifiableList(methodMetadata),
                Collections.unmodifiableList(new ArrayList<>(original.getAnnotations())));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getTestClassName() {
        return testClassName;
    }

    @Override
    public List<TestMethodMetadata> getTestMethodMetadata() {
        return testMethodMetadata;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultMetadataOnly that = (ResultMetadataOnly) o;
        return Objects.equals(name, that.name) && Objects.equals(packageName, that.packageName) && Objects.equals(testClassName, that.testClassName) && Objects.equals(testMethodMetadata, that.testMethodMetadata) && Objects.equals(annotations, that.annotations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, packageName, testClassName, testMethodMetadata, annotations);
    }

    @Override
    public String toString() {
        return "ResultMetadataOnly{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", testClassName='" + testClassName + '\'' +
                ", testMethodMetadata=" + testMethodMetadata +
                ", annotations=" + annotations +
                '}';
    }
}
