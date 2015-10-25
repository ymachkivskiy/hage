package org.jage.communication.hazelcast;


import com.hazelcast.core.*;
import lombok.extern.slf4j.Slf4j;
import org.jage.address.node.NodeAddress;
import org.jage.communication.common.RemoteCommunicationChannel;
import org.jage.communication.common.RemoteMessageSubscriber;
import org.jage.communication.message.ServiceMessage;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.collect.Sets.newCopyOnWriteArraySet;


@ThreadSafe
@Slf4j
class HazelcastRemoteCommunicationChannel<T extends ServiceMessage> implements RemoteCommunicationChannel<T> {

    private final AtomicLong conversationIdCounter = new AtomicLong(0);

    private final String chanelName;
    private final ITopic<T> broadcastTopic;
    private final IMap<Member, T> unicastMap;
    private final HazelcastInstance hazelcastInstance;

    private final Set<RemoteMessageSubscriber<T>> subscribers = newCopyOnWriteArraySet();

    public HazelcastRemoteCommunicationChannel(HazelcastInstance hazelcastInstance, String chanelName) {
        this.hazelcastInstance = hazelcastInstance;
        this.chanelName = chanelName;
        this.broadcastTopic = hazelcastInstance.getTopic(chanelName);
        this.unicastMap = hazelcastInstance.getMap("node-unicast-map-" + chanelName);

        this.unicastMap.addEntryListener(new UnicastListener(), hazelcastInstance.getCluster().getLocalMember(), true);
        this.broadcastTopic.addMessageListener(new BroadcastListener());
    }

    @Override
    public Long nextConversationId() {
        return conversationIdCounter.getAndIncrement();
    }

    @Override
    public void sendMessageToAll(@Nonnull final T message) {
        log.debug("Publishing [{}] on the channel [{}].", message, chanelName);
        broadcastTopic.publish(message);
    }

    @Override
    public void sendMessageToNode(T message, NodeAddress nodeAddress) {
        String localUid = hazelcastInstance.getCluster().getLocalMember().getUuid();
        if (nodeAddress.getIdentifier().equals(localUid)) {
            log.warn("Remote chanel client {} try to send message {} via chanel {} to himself", nodeAddress, message, chanelName);
        } else {
            Optional<Member> destinationMember = hazelcastInstance.getCluster().getMembers()
                    .stream()
                    .filter(m -> m.getUuid().equals(nodeAddress.getIdentifier()))
                    .findFirst();
            if (destinationMember.isPresent()) {
                log.debug("Send unicast message {} to node {}", message, nodeAddress);
                unicastMap.put(destinationMember.get(), message);
            } else {
                log.warn("Destination node {} is not connected. Can't send message {}", nodeAddress, message);
            }
        }
    }

    @Override
    public void subscribeChannel(@Nonnull RemoteMessageSubscriber<T> listener) {
        log.debug("Subscribe {} to the channel [{}].", listener, chanelName);
        subscribers.add(listener);
    }

    @Override
    public void unsubscribeChannel(@Nonnull RemoteMessageSubscriber<T> listener) {
        log.debug("Unsubscribe {} from the channel {}.", listener, this);

        subscribers.remove(listener);
    }

    private void notifyMessageReceived(T message) {
        subscribers.forEach(s -> s.onRemoteMessage(message));
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("name", broadcastTopic.getName()).toString();
    }

    private class BroadcastListener implements MessageListener<T> {
        @Override
        public void onMessage(@Nonnull final Message<T> message) {
            log.debug("Broadcast message [{}] on the channel [{}].", message, this);
            final T messageObject = message.getMessageObject();
            notifyMessageReceived(messageObject);
        }

    }

    private class UnicastListener implements EntryListener<Member, T> {
        @Override
        public void entryAdded(EntryEvent<Member, T> event) {
            onMessageReceivedEvent(event);
        }

        @Override
        public void entryUpdated(EntryEvent<Member, T> event) {
            onMessageReceivedEvent(event);
        }

        private void onMessageReceivedEvent(EntryEvent<Member, T> event) {
            T message = event.getValue();
            log.debug("Unicast message [{}] received on the chanel [{}]", message, chanelName);
            notifyMessageReceived(message);
        }

        @Override
        public void entryRemoved(EntryEvent<Member, T> event) {
        }

        @Override
        public void entryEvicted(EntryEvent<Member, T> event) {
        }
    }
}
