package org.hage.platform.rate.local.normalize;


import org.hage.platform.rate.local.config.NormalizationRateSettings;

public interface RateNormalizationProvider {
    RateNormalizer getNormalizer(NormalizationRateSettings configuration);
}
