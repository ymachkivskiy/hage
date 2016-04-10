package org.hage.platform.component;

import org.hage.platform.component.rate.BaseWeightPerformanceManager;
import org.hage.platform.component.rate.PerformanceManager;
import org.hage.platform.component.rate.measure.PerformanceMeasurer;
import org.hage.platform.component.rate.measure.impl.ConcurrencyPerformanceMeasurer;
import org.hage.platform.component.rate.measure.impl.MaxMemoryPerformanceMeasurer;
import org.hage.platform.component.rate.normalize.RateNormalizationProvider;
import org.hage.platform.component.rate.normalize.scaled.ScaledNormalizerProvider;
import org.hage.platform.component.rate.remote.ClusterPerformanceManager;
import org.hage.platform.component.rate.remote.ClusterPerformanceManagerEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateCoreCfg {

    @Bean
    public RateNormalizationProvider getRateNormalizationProvider() {
        return new ScaledNormalizerProvider();
    }

    @Bean
    public PerformanceManager getNodePerformanceManager() {
        return new BaseWeightPerformanceManager();
    }

    //region performance measurers

    @Bean
    public PerformanceMeasurer getMaxMemoryPerformanceMeasurer() {
        return new MaxMemoryPerformanceMeasurer();
    }

    @Bean
    public PerformanceMeasurer getConcurrencyPerformanceMeasurer() {
        return new ConcurrencyPerformanceMeasurer();
    }

    //endregion

    @Bean
    public ClusterPerformanceManager getClusterPerformanceManager() {
        return new ClusterPerformanceManagerEndpoint();
    }
}
