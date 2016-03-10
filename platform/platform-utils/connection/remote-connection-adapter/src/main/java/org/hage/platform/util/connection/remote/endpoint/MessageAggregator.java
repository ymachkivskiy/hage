package org.hage.platform.util.connection.remote.endpoint;

import java.io.Serializable;
import java.util.Collection;

public interface MessageAggregator<M extends Serializable, R> {
    R aggregate(Collection<MessageEnvelope<M>> messages);
}
