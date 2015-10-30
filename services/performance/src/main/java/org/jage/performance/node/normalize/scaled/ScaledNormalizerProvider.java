package org.jage.performance.node.normalize.scaled;

import org.jage.performance.node.config.NormalizationRateConfiguration;
import org.jage.performance.node.normalize.RateNormalizationProvider;
import org.jage.performance.node.normalize.RateNormalizer;

public class ScaledNormalizerProvider implements RateNormalizationProvider {
    @Override
    public RateNormalizer getNormalizer(NormalizationRateConfiguration configuration) {
        return new ScaledRateNormalizer(configuration.getGlobalRateConfiguration(), configuration.getRateConfiguration());
    }
}
