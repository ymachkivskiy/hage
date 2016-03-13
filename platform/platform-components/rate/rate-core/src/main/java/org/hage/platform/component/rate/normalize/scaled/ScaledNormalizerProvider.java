package org.hage.platform.component.rate.normalize.scaled;

import org.hage.platform.component.rate.config.data.NormalizationSettings;
import org.hage.platform.component.rate.normalize.RateNormalizationProvider;
import org.hage.platform.component.rate.normalize.RateNormalizer;

public class ScaledNormalizerProvider implements RateNormalizationProvider {
    @Override
    public RateNormalizer getNormalizer(NormalizationSettings configuration) {
        return new ScaledRateNormalizer(configuration.getGlobalRateSettings(), configuration.getMeasurerSettings());
    }
}
