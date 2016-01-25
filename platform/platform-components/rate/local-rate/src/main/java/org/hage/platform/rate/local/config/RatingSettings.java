package org.hage.platform.rate.local.config;

import org.hage.platform.rate.local.measure.PerformanceMeasurer;
import org.hage.platform.rate.local.normalize.PerformanceRate;

import static java.math.BigInteger.valueOf;

public class RatingSettings {

    private static final PerformanceRate MINIMAL_RATE = new PerformanceRate(valueOf(1));


    public boolean measurerEnabled(PerformanceMeasurer measurer) {
        return false;// TODO implement
    }

    public NormalizationRateSettings getNormalizationSettingsFor(PerformanceMeasurer measurer) {
        return new NormalizationRateSettings(getGlobalSettings(), getSettingsFor(measurer));
    }

    public PerformanceRate getMinimalRate() {
        return MINIMAL_RATE;
    }

    private MeasurerSettings getSettingsFor(PerformanceMeasurer measurer) {
        return null; //TODO implement
    }

    private GlobalRateSettings getGlobalSettings() {
        return null; //TODO
    }

}
