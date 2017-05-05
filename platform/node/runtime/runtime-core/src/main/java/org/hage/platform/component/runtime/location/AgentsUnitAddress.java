package org.hage.platform.component.runtime.location;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hage.platform.cluster.api.NodeAddress;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.connections.UnitAddress;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Optional.ofNullable;

@EqualsAndHashCode(doNotUseGetters = true, of = "position")
public class AgentsUnitAddress implements UnitAddress {

    private final Optional<NodeAddress> nodeAddress;
    @Getter
    private final Position position;

    private AgentsUnitAddress(NodeAddress nodeAddress, Position position) {
        checkNotNull(position);
        this.nodeAddress = ofNullable(nodeAddress);
        this.position = position;
    }

    public static AgentsUnitAddress onlineAddress(Position position, NodeAddress nodeAddress) {
        checkNotNull(nodeAddress);
        return new AgentsUnitAddress(nodeAddress, position);
    }

    public static AgentsUnitAddress offlineAddress(Position position) {
        return new AgentsUnitAddress(null, position);
    }

    public boolean isOnline() {
        return nodeAddress.isPresent();
    }

    public NodeAddress getNodeAddress() {
        return nodeAddress.get();
    }

    public AgentsUnitAddress activateWith(NodeAddress nodeAddress) {
        if (isOnline()) {
            return this;
        } else {
            return onlineAddress(position, nodeAddress);
        }
    }

    @Override
    public String getFriendlyIdentifier() {
        return position + "::[" + (nodeAddress.isPresent() ? nodeAddress.get() : "OFFLINE") + "]";
    }

    @Override
    public String toString() {
        return getFriendlyIdentifier();
    }

}
