package com.googlecode.yatspec.junit;

import jedi.functional.Filter;
import jedi.functional.Functor;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.annotation.Annotation;
import java.util.List;

import static java.util.Arrays.asList;
import static jedi.functional.FunctionalPrimitives.*;

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
        return flatten(computeTestMethods(), new Functor<FrameworkMethod, Iterable<FrameworkMethod>>() {
            public Iterable<FrameworkMethod> execute(final FrameworkMethod frameworkMethod) {
                final Table annotation = frameworkMethod.getAnnotation(Table.class);
                if (annotation == null) {
                    return asList(frameworkMethod);
                } else {
                    return collect(annotation.value(), new Functor<Row, FrameworkMethod>() {
                        public FrameworkMethod execute(Row row) {
                            return new DecoratingFrameworkMethod(frameworkMethod, row);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if(filter == null) {
            return super.computeTestMethods();
        }
        
        return select(super.computeTestMethods(), new Filter<FrameworkMethod>() {
            public Boolean execute(FrameworkMethod frameworkMethod) {
                return filter.shouldRun(describeChild(frameworkMethod));
            }
        });
    }

    @Override
    public void filter(org.junit.runner.manipulation.Filter filter) throws NoTestsRemainException {
        this.filter = filter;
        if(computeTestMethods().isEmpty()){
            throw new NoTestsRemainException();
        }
    }
}