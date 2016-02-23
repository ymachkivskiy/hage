package org.hage.platform.util.connection.adapter.aggregate;

import lombok.RequiredArgsConstructor;
import org.hage.platform.util.connection.chanel.Sender;
import org.hage.platform.util.connection.data.frame.MulticastFrame;

@RequiredArgsConstructor
public class AggregationChanelWrapper implements AggregationChanel {

    private final Sender sender;

    @Override
    public <T> T callAggregation(MulticastFrame frame, FrameAggregator<T> frameAggregator) {

        return null;
    }
}
