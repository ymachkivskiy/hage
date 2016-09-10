package org.hage.platform.component.rate.measure;


import lombok.Getter;
import org.hage.platform.component.rate.model.MeasurerType;

public abstract class PerformanceMeasurer {
    @Getter
    private final MeasurerType type;

    protected PerformanceMeasurer(MeasurerType type) {
        this.type = type;
    }

    public abstract int measureRate();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + type + ")";
    }
}
