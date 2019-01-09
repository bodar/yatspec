package com.googlecode.yatspec.junit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TableArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<Table> {
    private Table table;

    @Override
    public void accept(Table table) {
        this.table = table;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return stream(table.value()).map(resolveParameters(extensionContext));
    }

    private Function<Row, Arguments> resolveParameters(ExtensionContext extensionContext) {
        VarargsParameterResolver varargsParameterResolver = new VarargsParameterResolver();
        return row -> {
            try {
                return arguments(varargsParameterResolver.resolveParameters(row, null, extensionContext.getRequiredTestMethod()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
