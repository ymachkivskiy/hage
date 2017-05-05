package org.hage.platform.node.runtime.unitmove.remote;

import lombok.Data;
import org.hage.platform.node.runtime.unitmove.PackedUnit;

import java.io.Serializable;
import java.util.List;

import static org.hage.util.CollectionUtils.nullSafe;

@Data
class UnitMoveMessage implements Serializable {
    private final MessageType type;
    private final List<PackedUnit> packedUnits;

    private UnitMoveMessage(MessageType type, List<PackedUnit> packedUnits) {
        this.type = type;
        this.packedUnits = nullSafe(packedUnits);
    }

    public static UnitMoveMessage ack() {
        return new UnitMoveMessage(MessageType.ACK, null);
    }

    public static UnitMoveMessage move(List<PackedUnit> packedUnits) {
        return new UnitMoveMessage(MessageType.SEND, packedUnits);
    }

}
