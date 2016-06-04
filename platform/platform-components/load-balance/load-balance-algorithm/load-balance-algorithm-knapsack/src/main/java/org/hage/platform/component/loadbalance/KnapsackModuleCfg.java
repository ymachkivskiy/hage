package org.hage.platform.component.loadbalance;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.hage.platform.component.loadbalance.balancing.Balancer;
import org.hage.platform.component.loadbalance.balancing.GreedyBalancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Component;

@PlugableConfiguration
@ComponentScan(
    basePackageClasses = KnapsackModuleCfg.class,
    useDefaultFilters = false,
    includeFilters = @Filter(classes = Component.class)
)
public class KnapsackModuleCfg {

    @Bean
    public Balancer balancer() {
        return new GreedyBalancer();
    }

}
