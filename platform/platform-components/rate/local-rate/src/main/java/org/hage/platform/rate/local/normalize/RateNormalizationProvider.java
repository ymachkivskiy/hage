package org.hage.platform.rate.local.normalize;


import org.hage.platform.rate.local.config.data.NormalizationSettings;

public interface RateNormalizationProvider {
    RateNormalizer getNormalizer(NormalizationSettings configuration);
}
