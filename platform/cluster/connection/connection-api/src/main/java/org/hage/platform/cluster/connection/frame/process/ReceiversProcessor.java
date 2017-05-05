package org.hage.platform.cluster.connection.frame.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.cluster.connection.frame.AddressingType;
import org.hage.platform.cluster.connection.frame.Frame;
import org.hage.platform.cluster.connection.frame.Header;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor(access = PRIVATE)
public class ReceiversProcessor extends AbstractHeaderProcessor {

    private final Set<NodeAddress> receivers;
    private final AddressingType addressingType;

    @Override
    protected void updateHeader(Header.HeaderBuilder mutableHeader) {
        log.trace("Set receivers to {} ({}) for header {} ", receivers, addressingType, mutableHeader);

        mutableHeader
            .receivers(receivers)
            .addressingType(addressingType);
    }


    public static ReceiversProcessor broadcast() {
        return new ReceiversProcessor(emptySet(), AddressingType.BROADCAST);
    }

    public static ReceiversProcessor unicast(NodeAddress receiver) {
        return new ReceiversProcessor(singleton(receiver), AddressingType.UNICAST);
    }

    public static ReceiversProcessor multicast(Collection<NodeAddress> receivers) {
        return new ReceiversProcessor(new HashSet<>(receivers), AddressingType.MULTICAST);
    }

    public static ReceiversProcessor responseFor(Frame frame) {
        return unicast(frame.getHeader().getSender());
    }

}
