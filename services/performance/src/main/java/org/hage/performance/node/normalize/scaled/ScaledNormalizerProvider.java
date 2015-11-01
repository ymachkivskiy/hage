package org.hage.performance.node.normalize.scaled;

import org.hage.performance.node.config.NormalizationRateConfiguration;
import org.hage.performance.node.normalize.RateNormalizationProvider;
import org.hage.performance.node.normalize.RateNormalizer;

public class ScaledNormalizerProvider implements RateNormalizationProvider {
    @Override
    public RateNormalizer getNormalizer(NormalizationRateConfiguration configuration) {
        return new ScaledRateNormalizer(configuration.getGlobalRateConfiguration(), configuration.getRateConfiguration());
    }
}
