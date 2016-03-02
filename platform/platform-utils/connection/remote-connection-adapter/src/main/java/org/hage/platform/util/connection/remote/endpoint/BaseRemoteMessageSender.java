package org.hage.platform.util.connection.remote.endpoint;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hage.platform.util.connection.NodeAddress;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.chanel.FrameSender;

import java.io.Serializable;
import java.util.Collection;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor(access = PROTECTED)
public abstract class BaseRemoteMessageSender<M extends Serializable> implements ConnectionDescriber {

    private final ConnectionDescriptor connectionDescriptor;

    @Setter(PACKAGE)
    private FrameSender frameSender;


    @Override
    public ConnectionDescriptor describe() {
        return connectionDescriptor;
    }

    protected final void sendToAll(M message) {
        // TODO: 02.03.16 implement
    }

    protected final void sendTo(M message, Collection<NodeAddress> addresses) {
        // TODO: 02.03.16  implement
    }

    protected final void sendTo(M message, NodeAddress address) {
        // TODO: 02.03.16 implement
    }

    protected final <R> R sendToAllAndAggregateResponse(M message, MessageAggregator<M, R> aggregator) {
        // TODO: 02.03.16 implement
        return null;
    }

    protected final <R> R sendToAndAggregateResponses(M message, MessageAggregator<M, R> aggregator, Collection<NodeAddress> nodeAddresses) {
        // TODO: 02.03.16 implement
        return null;
    }
}
