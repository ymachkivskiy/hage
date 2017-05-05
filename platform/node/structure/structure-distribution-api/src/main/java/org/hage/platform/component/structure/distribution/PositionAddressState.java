package org.hage.platform.component.structure.distribution;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hage.platform.cluster.api.NodeAddress;

import static org.hage.platform.component.structure.distribution.PositionState.*;

@ToString
@RequiredArgsConstructor
public class PositionAddressState {
    public static final PositionAddressState NOT_CORRECT_POSITION_ADDRESS = new PositionAddressState(NOT_CORRECT, null);

    private final PositionState state;
    private final NodeAddress address;

    public boolean hasAddress() {
        return state == ACTIVE;
    }

    public boolean isCorrect() {
        return state != NOT_CORRECT;
    }

    public boolean isNotActive() {
        return state == NOT_ACTIVE;
    }

    public NodeAddress getAddress() {
        return address;
    }

}
