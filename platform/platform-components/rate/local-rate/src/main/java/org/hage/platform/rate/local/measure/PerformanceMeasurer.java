package org.hage.platform.rate.local.measure;


import org.hage.platform.rate.model.MeasurerType;

public interface PerformanceMeasurer {
    MeasurerType getType();

    int measureRate();
}
