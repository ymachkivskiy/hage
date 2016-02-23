package org.hage.platform.util.connection.data.header;

import lombok.Data;
import org.hage.platform.util.connection.address.NodeAddress;

import java.util.Set;

@Data
public class MulticastHeader extends Header {

    private final Set<NodeAddress> receivers;

}
