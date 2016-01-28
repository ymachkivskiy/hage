package org.hage.platform.rate.local.normalize.scaled;

import org.hage.platform.rate.local.config.NormalizationSettings;
import org.hage.platform.rate.local.normalize.RateNormalizationProvider;
import org.hage.platform.rate.local.normalize.RateNormalizer;

public class ScaledNormalizerProvider implements RateNormalizationProvider {
    @Override
    public RateNormalizer getNormalizer(NormalizationSettings configuration) {
        return new ScaledRateNormalizer(configuration.getGlobalRateSettings(), configuration.getMeasurerSettings());
    }
}
