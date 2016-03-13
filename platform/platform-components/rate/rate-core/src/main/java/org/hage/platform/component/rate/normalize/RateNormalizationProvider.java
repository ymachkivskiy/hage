package org.hage.platform.component.rate.normalize;


import org.hage.platform.component.rate.config.data.NormalizationSettings;

public interface RateNormalizationProvider {
    RateNormalizer getNormalizer(NormalizationSettings configuration);
}
