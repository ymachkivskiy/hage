package org.hage.platform.util.connection.remote.api;

import org.hage.platform.util.connection.NodeAddress;

import java.io.Serializable;
import java.util.Collection;

public interface ResultAggregationSender<M extends Serializable, R> {
    R sendToAllAndAggregateResponses(M message, MessageAggregator<M, R> aggregator);

    R sendToAndAggregateResponses(M message, MessageAggregator<M, R> aggregator, Collection<NodeAddress> nodeAddresses);
}
