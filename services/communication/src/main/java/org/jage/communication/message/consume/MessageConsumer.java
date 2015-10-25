package org.jage.communication.message.consume;

import org.jage.communication.message.ServiceMessage;

public interface MessageConsumer<RemoteMessageT extends ServiceMessage> {
    void consumeMessage(RemoteMessageT message);
}
