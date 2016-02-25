package org.hage.platform.util.connection.adapter.aggregate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AggregationChanelWrapper implements AggregationChanel {


    @Override
    public <T> T callAggregation(MulticastFrame frame, FrameAggregator<T> frameAggregator) {

        return null;
    }
}
