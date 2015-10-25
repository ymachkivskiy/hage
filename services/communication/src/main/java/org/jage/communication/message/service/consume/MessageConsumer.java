package org.jage.communication.message.service.consume;

import org.jage.communication.message.service.ServiceMessage;

public interface MessageConsumer<RemoteMessageT extends ServiceMessage> {
    void consumeMessage(RemoteMessageT message);
}
