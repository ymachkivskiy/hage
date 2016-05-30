package org.hage.platform.component.loadbalance;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Component;

@PlugableConfiguration
@ComponentScan(
    basePackageClasses = KnapsackModuleCfg.class,
    useDefaultFilters = false,
    includeFilters = @Filter(classes = Component.class)
)
class KnapsackModuleCfg {
}
