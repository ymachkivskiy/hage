package org.jage.communication.hazelcast;


import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import lombok.extern.slf4j.Slf4j;
import org.jage.communication.api.CommunicationChannel;
import org.jage.communication.api.MessageSubscriber;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.collect.Sets.newCopyOnWriteArraySet;


@ThreadSafe
@Slf4j
class HazelcastCommunicationChannel<T> implements CommunicationChannel<T> {

    @Nonnull
    private final ITopic<T> topic;

    private final Set<MessageSubscriber<T>> subscribers = newCopyOnWriteArraySet();

    public HazelcastCommunicationChannel(@Nonnull final ITopic<T> topic) {
        this.topic = topic;
        topic.addMessageListener(new Listener());
    }

    @Override
    public void publish(@Nonnull final T message) {
        log.debug("Publishing {} on the channel {}.", message, this);

        topic.publish(message);
    }

    @Override
    public void subscribe(@Nonnull MessageSubscriber<T> listener) {
        log.debug("Subscribe {} to the channel {}.", listener, this);

        subscribers.add(listener);
    }

    @Override
    public void unsubscribe(@Nonnull MessageSubscriber<T> listener) {
        log.debug("Unsubscribe {} from the channel {}.", listener, this);

        subscribers.remove(listener);
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("name", topic.getName()).toString();
    }

    private class Listener implements MessageListener<T> {

        @Override
        public void onMessage(@Nonnull final Message<T> message) {
            log.debug("Message {} on the channel {}.", message, this);

            final T messageObject = message.getMessageObject();
            for(final MessageSubscriber<T> subscriber : subscribers) {
                subscriber.onMessage(messageObject);
            }
        }
    }
}
