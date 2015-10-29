package org.jage.performance.rate.normalize.scaled;

import org.jage.performance.rate.normalize.config.RateConfiguration;
import org.jage.performance.rate.normalize.RateNormalizationProvider;
import org.jage.performance.rate.normalize.RateNormalizer;

public class Provider implements RateNormalizationProvider {
    @Override
    public RateNormalizer getNormalizer(RateConfiguration configuration) {
        return new ScaledRateNormalizer(configuration.getGlobalRateConfig(), configuration.getCategoryConfiguration());
    }
}
