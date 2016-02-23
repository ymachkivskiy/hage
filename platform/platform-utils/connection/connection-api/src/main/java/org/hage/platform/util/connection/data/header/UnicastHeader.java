package org.hage.platform.util.connection.data.header;

import lombok.Data;
import org.hage.platform.util.connection.address.NodeAddress;

@Data
public class UnicastHeader extends Header {
    private final NodeAddress receiver;
}
