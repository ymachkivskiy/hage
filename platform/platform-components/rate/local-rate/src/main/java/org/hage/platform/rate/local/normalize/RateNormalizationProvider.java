package org.hage.platform.rate.local.normalize;


public interface RateNormalizationProvider {
    RateNormalizer getNormalizer(NormalizationRateSettings configuration);
}
