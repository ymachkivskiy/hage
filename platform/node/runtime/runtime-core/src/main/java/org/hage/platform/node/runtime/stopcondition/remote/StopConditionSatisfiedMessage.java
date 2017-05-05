package org.hage.platform.node.runtime.stopcondition.remote;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;
import static org.hage.platform.node.runtime.stopcondition.remote.StopConditionSatisfiedMessage.MessageType.ACK;
import static org.hage.platform.node.runtime.stopcondition.remote.StopConditionSatisfiedMessage.MessageType.NOTIFY;

@ToString
@RequiredArgsConstructor(access = PRIVATE)
class StopConditionSatisfiedMessage implements Serializable {
    private final MessageType type;

    public static StopConditionSatisfiedMessage notification() {
        return new StopConditionSatisfiedMessage(NOTIFY);
    }

    public static StopConditionSatisfiedMessage ack() {
        return new StopConditionSatisfiedMessage(ACK);
    }

    enum MessageType implements Serializable {
        ACK,
        NOTIFY,
    }

}
