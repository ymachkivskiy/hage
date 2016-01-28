package org.hage.platform.rate.local;

import org.hage.platform.rate.local.measure.PerformanceMeasurer;
import org.hage.platform.rate.local.measure.impl.ConcurrencyPerformanceMeasurer;
import org.hage.platform.rate.local.measure.impl.MaxMemoryPerformanceMeasurer;
import org.hage.platform.rate.local.normalize.RateNormalizationProvider;
import org.hage.platform.rate.local.normalize.scaled.ScaledNormalizerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = PlatformRateLocalModuleConfiguration.class)
class PlatformRateLocalModuleConfiguration {

    @Bean
    public RateNormalizationProvider getRateNormalizationProvider() {
        return new ScaledNormalizerProvider();
    }

    @Bean
    public LocalPerformanceManager getNodePerformanceManager() {
        return new WeightBasedLocalPerformanceManager();
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

}
