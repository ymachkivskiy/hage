
package org.jage.communication.message.service;


import lombok.Getter;
import lombok.Setter;
import org.jage.address.node.NodeAddress;

import java.io.Serializable;


public class ServiceHeader<E extends Enum<E>> implements Serializable {
    @Getter
    private final E type;
    @Getter
    private Long conversationId;
    @Getter
    @Setter
    private NodeAddress sender;

    private ServiceHeader(E type, Long conversationId) {
        this.type = type;
        this.conversationId = conversationId;
    }

    public static <Z extends Enum<Z>> ServiceHeader<Z> create(Z type) {
        return create(type, null);
    }

    public static  <Z extends Enum<Z>> ServiceHeader<Z> create(Z type, Long conversationId) {
        return new ServiceHeader<>(type, conversationId);
    }

}
