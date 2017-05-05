package org.hage.platform.node.rate.measure.normalize.scaled;

import org.hage.platform.node.rate.config.data.NormalizationSettings;
import org.hage.platform.node.rate.measure.normalize.RateNormalizationProvider;
import org.hage.platform.node.rate.measure.normalize.RateNormalizer;

public class ScaledNormalizerProvider implements RateNormalizationProvider {
    @Override
    public RateNormalizer getNormalizer(NormalizationSettings configuration) {
        return new ScaledRateNormalizer(configuration.getGlobalRateSettings(), configuration.getMeasurerSettings());
    }
}
