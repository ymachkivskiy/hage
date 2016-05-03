package org.hage.platform.util.connection.hazelcast.chanel;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.cluster.LocalClusterNode;
import org.hage.platform.component.cluster.NodeAddress;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.chanel.FrameSender;
import org.hage.platform.util.connection.frame.Frame;
import org.hage.platform.util.connection.frame.process.FrameProcessor;
import org.hage.platform.util.connection.frame.process.SenderProcessor;

import java.util.Collection;
import java.util.HashSet;

import static lombok.AccessLevel.PACKAGE;
import static org.hage.platform.util.connection.frame.util.FrameUtil.*;

@RequiredArgsConstructor
@Slf4j
class HazelcastSender implements FrameSender {

    private final ConnectionDescriptor descriptor;
    private final LocalClusterNode localClusterNode;
    private final HazelcastInstance hazelcastInstance;


    @Setter(PACKAGE)
    private HazelcastReceiveAdapter receiver;

    private FrameProcessor frameProcessor;
    private ITopic<Frame> broadcastTopic;
    private IMap<NodeAddress, Frame> unicastMap;


    void initialize() {
        this.broadcastTopic = hazelcastInstance.getTopic(descriptor.getChanelName());
        this.unicastMap = hazelcastInstance.getMap("node-unicast-map-" + descriptor.getChanelName());

        this.broadcastTopic.addMessageListener(new BroadcastFrameTopicListener(receiver));
        this.unicastMap.addEntryListener(new UnicastFrameMapListener(receiver), localClusterNode.getLocalAddress(), true);

        this.frameProcessor = new SenderProcessor(localClusterNode.getLocalAddress());
    }

    @Override
    public void send(Frame frame) {
        log.debug("Sending frame {} with chanel {}", frame, descriptor);

        frame = frameProcessor.process(frame);

        if (isBroadcastFrame(frame)) {
            broadcast(frame);
        } else {
            for (NodeAddress address : getUniqueAddressesForFrame(frame)) {
                unicastToNode(frame, address);
            }
        }

        log.debug("Frame sent.");
    }

    private void broadcast(Frame frame) {
        log.debug("Broadcasting {} with chanel {}", frame, descriptor);

        broadcastTopic.publish(frame);
    }

    private void unicastToNode(Frame frame, NodeAddress address) {
        log.debug("Sending unicast frame {} to node {} with chanel {} ", frame, address, descriptor);

        if (localClusterNode.getLocalAddress().equals(address)) {
            if (isDeliverableToSender(frame)) {
                log.debug("Send to loopback");
                receiver.receive(frame);
            } else {
                log.debug("Frame is not supposed to be received by its sender");
            }
        } else {
            unicastMap.putAsync(address, frame);
        }
    }

    private Collection<NodeAddress> getUniqueAddressesForFrame(Frame frame) {
        return new HashSet<>(getReceiverAdresses(frame));
    }

}
