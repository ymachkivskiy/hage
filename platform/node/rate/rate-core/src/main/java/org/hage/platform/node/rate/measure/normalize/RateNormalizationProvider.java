package org.hage.platform.node.rate.measure.normalize;


import org.hage.platform.node.rate.config.data.NormalizationSettings;

public interface RateNormalizationProvider {
    RateNormalizer getNormalizer(NormalizationSettings configuration);
}
