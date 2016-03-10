package org.hage.platform.communication.address.agent;


import org.hage.platform.communication.address.AbstractAddress;
import org.hage.platform.communication.address.NodeAddress;

import javax.annotation.concurrent.Immutable;
import java.util.UUID;


@Immutable
public class DefaultAgentAddress extends AbstractAddress implements AgentAddress {

    public DefaultAgentAddress(NodeAddress nodeAddress) {
        super(UUID.randomUUID().toString(), nodeAddress);
    }

    public DefaultAgentAddress(final NodeAddress nodeAddress, final String friendlyName) {
        super(UUID.randomUUID().toString(), nodeAddress, friendlyName);
    }
}
