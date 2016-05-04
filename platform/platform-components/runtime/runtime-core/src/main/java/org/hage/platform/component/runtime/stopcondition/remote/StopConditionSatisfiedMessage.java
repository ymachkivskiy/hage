package org.hage.platform.component.runtime.stopcondition.remote;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;
import static org.hage.platform.component.runtime.stopcondition.remote.MessageType.ACK;
import static org.hage.platform.component.runtime.stopcondition.remote.MessageType.NOTIFY;

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

}
