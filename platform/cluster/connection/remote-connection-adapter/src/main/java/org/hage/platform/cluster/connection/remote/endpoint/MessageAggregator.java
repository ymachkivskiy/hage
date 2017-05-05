package org.hage.platform.cluster.connection.remote.endpoint;

import java.io.Serializable;
import java.util.List;

public interface MessageAggregator<M extends Serializable, R> {
    R aggregate(List<MessageEnvelope<M>> messages);
}
