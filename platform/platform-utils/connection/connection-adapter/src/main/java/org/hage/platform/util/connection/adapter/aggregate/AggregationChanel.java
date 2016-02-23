package org.hage.platform.util.connection.adapter.aggregate;

import org.hage.platform.util.connection.data.frame.MulticastFrame;

public interface AggregationChanel {
    <T> T callAggregation(MulticastFrame frame, FrameAggregator<T> frameAggregator);
}
