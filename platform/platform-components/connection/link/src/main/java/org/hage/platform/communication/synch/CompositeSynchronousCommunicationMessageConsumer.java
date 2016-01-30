package org.hage.platform.communication.synch;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.communication.message.service.ServiceHeader;
import org.hage.platform.communication.message.service.ServiceMessage;
import org.hage.platform.communication.message.service.consume.MessageConsumer;

import java.util.LinkedList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Slf4j
class CompositeSynchronousCommunicationMessageConsumer<RemoteMessageT extends ServiceMessage> implements MessageConsumer<RemoteMessageT> {

    private List<MessageConsumer<RemoteMessageT>> consumers = new LinkedList<>();

    @Override
    public void consumeMessage(RemoteMessageT message) {
        if (isPartOfSynchronousCommunication(message)) {
            log.debug("Consume message {}", message);
            consumers.forEach(mc -> mc.consumeMessage(message));
        }
    }

    public void registerMessageConsumer(MessageConsumer<RemoteMessageT> messageConsumer) {
        log.debug("Add message consumer {}", messageConsumer);
        consumers.add(messageConsumer);
    }

    public void unregisterMessageConsumer(MessageConsumer<RemoteMessageT> messageConsumer) {
        log.debug("Remove message consumer {}", messageConsumer);
        consumers.remove(messageConsumer);
    }

    private boolean isPartOfSynchronousCommunication(RemoteMessageT message) {
        log.debug("Checking if message {} is part of synchronous communication", message);
        return ofNullable(message)
                .map(ServiceMessage::getHeader)
                .map(ServiceHeader::getConversationId)
                .isPresent();
    }
}
