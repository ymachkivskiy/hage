package org.hage.platform.component.structure.address;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;

import java.util.Optional;

@Data
public class PositionAddressingInfo {
    private final PositionState state;
    private final Optional<NodeAddress> address;
}
