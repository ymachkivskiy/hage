package org.hage.platform.node.structure.distribution;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hage.platform.cluster.api.NodeAddress;

@ToString
@RequiredArgsConstructor
public class PositionAddressState {
    public static final PositionAddressState NOT_CORRECT_POSITION_ADDRESS = new PositionAddressState(PositionState.NOT_CORRECT, null);

    private final PositionState state;
    private final NodeAddress address;

    public boolean hasAddress() {
        return state == PositionState.ACTIVE;
    }

    public boolean isCorrect() {
        return state != PositionState.NOT_CORRECT;
    }

    public boolean isNotActive() {
        return state == PositionState.NOT_ACTIVE;
    }

    public NodeAddress getAddress() {
        return address;
    }

}
