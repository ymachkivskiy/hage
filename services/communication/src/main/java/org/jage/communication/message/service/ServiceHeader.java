
package org.jage.communication.message.service;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jage.address.node.NodeAddress;

import java.io.Serializable;


@ToString
public class ServiceHeader<E extends Enum<E>> implements Serializable {
    @Getter
    private final E type;
    @Getter
    @Setter
    private Long conversationId;
    @Getter
    @Setter
    private NodeAddress sender;

    private ServiceHeader(E type, Long conversationId, NodeAddress sender) {
        this.type = type;
        this.conversationId = conversationId;
        this.sender = sender;
    }

    public static <Z extends Enum<Z>> ServiceHeader<Z> create(Z type) {
        return create(type, null, null);
    }

    public static <Z extends Enum<Z>> ServiceHeader<Z> create(Z type, Long conversationId) {
        return create(type, conversationId, null);
    }

    public static <Z extends Enum<Z>> ServiceHeader<Z> create(Z type, NodeAddress sender) {
        return create(type, null, sender);
    }

    public static <Z extends Enum<Z>> ServiceHeader<Z> create(Z type, Long conversationId, NodeAddress sender) {
        return new ServiceHeader<>(type, conversationId, sender);
    }

}
