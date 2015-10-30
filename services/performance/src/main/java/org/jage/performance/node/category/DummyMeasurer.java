package org.jage.performance.node.category;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DummyMeasurer implements PerformanceMeasurer {

    @Override
    public int measureRate() {
        return 1;
    }
}
