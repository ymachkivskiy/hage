package org.hage.platform.util.connection.remote.endpoint;

import lombok.Data;
import org.hage.platform.component.cluster.NodeAddress;

import java.io.Serializable;

@Data
public class MessageEnvelope<M extends Serializable> {
    private final NodeAddress origin;
    private final M body;
    private final boolean isLocalMessage;
}
