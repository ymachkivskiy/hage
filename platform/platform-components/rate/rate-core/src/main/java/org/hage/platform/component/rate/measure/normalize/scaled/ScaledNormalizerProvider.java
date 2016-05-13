package org.hage.platform.component.rate.measure.normalize.scaled;

import org.hage.platform.component.rate.config.data.NormalizationSettings;
import org.hage.platform.component.rate.measure.normalize.RateNormalizationProvider;
import org.hage.platform.component.rate.measure.normalize.RateNormalizer;

public class ScaledNormalizerProvider implements RateNormalizationProvider {
    @Override
    public RateNormalizer getNormalizer(NormalizationSettings configuration) {
        return new ScaledRateNormalizer(configuration.getGlobalRateSettings(), configuration.getMeasurerSettings());
    }
}
