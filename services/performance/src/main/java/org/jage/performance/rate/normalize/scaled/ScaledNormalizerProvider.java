package org.jage.performance.rate.normalize.scaled;

import org.jage.performance.rate.normalize.config.NormalizationRateConfiguration;
import org.jage.performance.rate.normalize.RateNormalizationProvider;
import org.jage.performance.rate.normalize.RateNormalizer;

public class ScaledNormalizerProvider implements RateNormalizationProvider {
    @Override
    public RateNormalizer getNormalizer(NormalizationRateConfiguration configuration) {
        return new ScaledRateNormalizer(configuration.getGlobalRateConfiguration(), configuration.getRateConfiguration());
    }
}
