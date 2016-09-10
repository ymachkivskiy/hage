package org.hage.platform.component;

import org.hage.platform.component.rate.measure.PerformanceMeasurer;
import org.hage.platform.component.rate.measure.impl.ConcurrencyPerformanceMeasurer;
import org.hage.platform.component.rate.measure.impl.MaxMemoryPerformanceMeasurer;
import org.hage.platform.component.rate.measure.normalize.RateNormalizationProvider;
import org.hage.platform.component.rate.measure.normalize.scaled.ScaledNormalizerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateCoreCfg {

    @Bean
    public RateNormalizationProvider getRateNormalizationProvider() {
        return new ScaledNormalizerProvider();
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
