package org.hage.platform.component.simulationconfig;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public abstract class CoreConfigurerAdapter<T> implements CoreConfigurer {

    private final Function<Configuration, T> configurationExtractor;
    private final int order;

    @Override
    public final void configureWith(Configuration configuration) {
        configureWithNarrow(configurationExtractor.apply(configuration));
    }

    @Override
    public int getOrder() {
        return order;
    }

    protected abstract void configureWithNarrow(T narrowConfiguration);

}
