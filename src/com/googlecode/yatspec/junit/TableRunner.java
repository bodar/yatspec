package com.googlecode.yatspec.junit;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class TableRunner extends BlockJUnit4ClassRunner {
    private org.junit.runner.manipulation.Filter  filter;

    public TableRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
        // skip
    }

    @Override
    protected List<FrameworkMethod> getChildren() {
        return sequence(computeTestMethods()).flatMap(new Callable1<FrameworkMethod, Iterable<FrameworkMethod>>() {
            public Iterable<FrameworkMethod> call(final FrameworkMethod frameworkMethod) {
                final Table annotation = frameworkMethod.getAnnotation(Table.class);
                if (annotation == null) {
                    return sequence(frameworkMethod);
                } else {
                    return sequence(annotation.value()).map(decorateTestMethod(frameworkMethod));
                }
            }
        }).toList();
    }

    private static Callable1<Row, FrameworkMethod> decorateTestMethod(final FrameworkMethod frameworkMethod) {
        return new Callable1<Row, FrameworkMethod>() {
            public FrameworkMethod call(Row row) {
                return new DecoratingFrameworkMethod(frameworkMethod, row);
            }
        };
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if(filter == null) {
            return super.computeTestMethods();
        }
        
        return sequence(super.computeTestMethods()).filter(new Predicate<FrameworkMethod>() {
            public boolean matches(FrameworkMethod frameworkMethod) {
                return filter.shouldRun(describeChild(frameworkMethod));
            }
        }).toList();
    }

    @Override
    public void filter(org.junit.runner.manipulation.Filter filter) throws NoTestsRemainException {
        this.filter = filter;
        if(computeTestMethods().isEmpty()){
            throw new NoTestsRemainException();
        }
    }
}