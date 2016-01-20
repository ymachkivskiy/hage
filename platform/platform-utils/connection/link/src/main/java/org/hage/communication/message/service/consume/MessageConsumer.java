package org.hage.communication.message.service.consume;

import org.hage.communication.message.service.ServiceMessage;

public interface MessageConsumer<RemoteMessageT extends ServiceMessage> {
    void consumeMessage(RemoteMessageT message);
}
