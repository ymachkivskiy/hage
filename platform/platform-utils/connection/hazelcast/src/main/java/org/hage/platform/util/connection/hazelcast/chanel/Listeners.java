package org.hage.platform.util.connection.hazelcast.chanel;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import lombok.RequiredArgsConstructor;
import org.hage.platform.util.connection.NodeAddress;
import org.hage.platform.util.connection.chanel.Receiver;
import org.hage.platform.util.connection.frame.Frame;

@RequiredArgsConstructor
class BroadcastFrameTopicListener implements MessageListener<Frame> {

    private final Receiver frameReceiver;

    @Override
    public void onMessage(Message<Frame> message) {
        frameReceiver.receive(message.getMessageObject());
    }

}

@RequiredArgsConstructor
class UnicastFrameMapListener implements EntryListener<NodeAddress, Frame> {

    private final Receiver frameReceiver;

    @Override
    public void entryAdded(EntryEvent<NodeAddress, Frame> event) {
        frameReceiver.receive(event.getValue());
    }

    @Override
    public void entryUpdated(EntryEvent<NodeAddress, Frame> event) {
        frameReceiver.receive(event.getValue());
    }

    @Override
    public void entryRemoved(EntryEvent<NodeAddress, Frame> event) { /* no-op */ }

    @Override
    public void entryEvicted(EntryEvent<NodeAddress, Frame> event) { /* no-op */ }

}
