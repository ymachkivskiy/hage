package org.jage.communication.message.consume;

import lombok.extern.slf4j.Slf4j;
import org.jage.communication.message.ServiceMessage;

@Slf4j
public abstract class BaseConditionalMessageConsumer<RemoteMessageT extends ServiceMessage> implements MessageConsumer<RemoteMessageT> {

    @Override
    public final void consumeMessage(RemoteMessageT message) {
        log.debug("Got message");

        if (messageMatches(message)) {
            log.debug("Consuming matching message {}", message);

            consumeMatchingMessage(message);
        }
    }

    protected abstract void consumeMatchingMessage(RemoteMessageT message);

    protected abstract boolean messageMatches(RemoteMessageT message);
}
