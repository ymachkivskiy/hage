package org.hage.platform.util.communication.message.service.consume;

import org.hage.platform.util.communication.message.service.ServiceMessage;

public interface MessageConsumer<RemoteMessageT extends ServiceMessage> {
    void consumeMessage(RemoteMessageT message);
}
