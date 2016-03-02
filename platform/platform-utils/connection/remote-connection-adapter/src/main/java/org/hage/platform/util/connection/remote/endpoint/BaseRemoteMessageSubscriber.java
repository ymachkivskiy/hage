package org.hage.platform.util.connection.remote.endpoint;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;

import java.io.Serializable;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor(access = PROTECTED)
public abstract class BaseRemoteMessageSubscriber<M extends Serializable, R extends Serializable> implements ConnectionDescriber {

    private final ConnectionDescriptor connectionDescriptor;
    @Getter(PACKAGE)
    private final Class<M> messageClazz;

    @Override
    public ConnectionDescriptor describe() {
        return connectionDescriptor;
    }

    protected abstract R consumeAndRespond(RemoteMessage<M> remoteMessage);

    protected abstract void consume(RemoteMessage<M> remoteMessage);
}
