package org.jage.communication.message.consume;

import lombok.extern.slf4j.Slf4j;
import org.jage.communication.message.ServiceMessage;

import java.util.LinkedList;
import java.util.List;

//TODO check thread safety
@Slf4j
public class CompositeMessageConsumer<RemoteMessageT extends ServiceMessage> implements MessageConsumer<RemoteMessageT> {

    private List<MessageConsumer<RemoteMessageT>> messageConsumers = new LinkedList<>();

    @Override
    public void consumeMessage(RemoteMessageT message) {
        log.debug("Consume message {}", message);
        messageConsumers.forEach(mc -> mc.consumeMessage(message));
    }

    public void registerMessageConsumer(MessageConsumer<RemoteMessageT> messageConsumer) {
        log.debug("Register new message consumer {}", messageConsumer);
        messageConsumers.add(messageConsumer);
    }
}
