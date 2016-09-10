package org.hage.platform.component.rate.measure.normalize;


import org.hage.platform.component.rate.config.data.NormalizationSettings;

public interface RateNormalizationProvider {
    RateNormalizer getNormalizer(NormalizationSettings configuration);
}
