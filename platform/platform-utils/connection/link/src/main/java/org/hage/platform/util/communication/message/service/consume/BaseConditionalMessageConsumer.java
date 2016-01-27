package org.hage.platform.util.communication.message.service.consume;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.util.communication.message.service.ServiceMessage;

@Slf4j
public abstract class BaseConditionalMessageConsumer<RemoteMessageT extends ServiceMessage> implements MessageConsumer<RemoteMessageT> {

    @Override
    public final void consumeMessage(RemoteMessageT message) {
        log.debug("Got message {}", message);

        if (messageMatches(message)) {
            log.debug("Message matching. Consuming.", message);

            consumeMatchingMessage(message);
        }
    }

    protected abstract void consumeMatchingMessage(RemoteMessageT message);

    protected abstract boolean messageMatches(RemoteMessageT message);
}
