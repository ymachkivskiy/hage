package org.hage.platform.util.connection.remote.endpoint;

import lombok.Data;
import org.hage.platform.util.connection.NodeAddress;

import java.io.Serializable;

@Data
public class RemoteMessage<M extends Serializable> {
    private final NodeAddress origin;
    private final M message;
}
