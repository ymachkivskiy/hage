package org.hage.platform.util.connection.adapter.aggregate;

public interface AggregationChanel {
    <T> T callAggregation(MulticastFrame frame, FrameAggregator<T> frameAggregator);
}
