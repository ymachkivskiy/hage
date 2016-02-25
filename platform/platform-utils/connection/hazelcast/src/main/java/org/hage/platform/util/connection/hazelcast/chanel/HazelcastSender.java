package org.hage.platform.util.connection.hazelcast.chanel;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.util.connection.LocalNodeAddressProvider;
import org.hage.platform.util.connection.NodeAddress;
import org.hage.platform.util.connection.chanel.ConnectionDescriptor;
import org.hage.platform.util.connection.chanel.FrameSender;
import org.hage.platform.util.connection.frame.Frame;
import org.hage.platform.util.connection.frame.process.FrameProcessor;
import org.hage.platform.util.connection.frame.process.SenderProcessor;
import org.hage.platform.util.connection.frame.util.FrameUtil;

import java.util.Collection;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PACKAGE;
import static org.hage.platform.util.connection.frame.util.FrameUtil.isBroadcastFrame;

@RequiredArgsConstructor
@Slf4j
class HazelcastSender implements FrameSender {

    private final ConnectionDescriptor descriptor;
    private final LocalNodeAddressProvider localNodeAddressProvider;
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
        this.unicastMap.addEntryListener(new UnicastFrameMapListener(receiver), localNodeAddressProvider.getLocalAddress(), true);

        this.frameProcessor = new SenderProcessor(localNodeAddressProvider.getLocalAddress());
    }

    @Override
    public void send(Frame frame) {
        log.debug("Sending frame {} with chanel {}", frame, descriptor);

        frame = frameProcessor.process(frame);

        if (isBroadcastFrame(frame)) {
            broadcast(frame);
        } else {
            for (NodeAddress address : getUniqueRemoteAddressesForFrame(frame)) {
                unicastToNode(frame, address);
            }
        }

    }

    private void broadcast(Frame frame) {
        log.debug("Broadcasting {} with chanel {}", frame, descriptor);

        broadcastTopic.publish(frame);
    }

    private void unicastToNode(Frame frame, NodeAddress address) {
        log.debug("Sending frame {} with chanel {} to node {}", frame, descriptor, address);

        unicastMap.putAsync(address, frame);
    }

    private Collection<NodeAddress> getUniqueRemoteAddressesForFrame(Frame frame) {
        return FrameUtil.getReceiverAdresses(frame).stream()
            .filter(receiverAddress -> !localNodeAddressProvider.getLocalAddress().equals(receiverAddress))
            .distinct()
            .collect(toList());
    }

}
