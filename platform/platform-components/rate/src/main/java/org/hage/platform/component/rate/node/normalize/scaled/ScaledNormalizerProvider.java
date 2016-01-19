package org.hage.platform.component.rate.node.normalize.scaled;

import org.hage.platform.component.rate.node.config.NormalizationRateConfiguration;
import org.hage.platform.component.rate.node.normalize.RateNormalizationProvider;
import org.hage.platform.component.rate.node.normalize.RateNormalizer;

public class ScaledNormalizerProvider implements RateNormalizationProvider {
    @Override
    public RateNormalizer getNormalizer(NormalizationRateConfiguration configuration) {
        return new ScaledRateNormalizer(configuration.getGlobalRateConfiguration(), configuration.getRateConfiguration());
    }
}
