package org.hage.platform.cluster.loadbalance.knapsack;

import org.hage.platform.annotation.di.PlugableConfiguration;
import org.hage.platform.cluster.loadbalance.knapsack.balancing.pack.AllocationsPacker;
import org.hage.platform.cluster.loadbalance.knapsack.balancing.pack.GreedyAllocationsPacker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@PlugableConfiguration
@ComponentScan(
    basePackageClasses = KnapsackModuleCfg.class,
    useDefaultFilters = false,
    includeFilters = @Filter(classes = Component.class)
)
@EnableAspectJAutoProxy
public class KnapsackModuleCfg {

    @Bean
    public AllocationsPacker balancer() {
        return new GreedyAllocationsPacker();
    }

}
