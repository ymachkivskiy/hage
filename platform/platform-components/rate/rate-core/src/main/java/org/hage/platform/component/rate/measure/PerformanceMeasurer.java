package org.hage.platform.component.rate.measure;


import org.hage.platform.component.rate.model.MeasurerType;

public interface PerformanceMeasurer {
    MeasurerType getType();

    int measureRate();
}
