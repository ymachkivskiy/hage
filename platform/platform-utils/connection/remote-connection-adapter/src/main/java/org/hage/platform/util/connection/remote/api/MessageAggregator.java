package org.hage.platform.util.connection.remote.api;

import java.io.Serializable;
import java.util.Collection;

public interface MessageAggregator<M extends Serializable, R> {
    R aggregate(Collection<RemoteMessage<M>> messages);
}
