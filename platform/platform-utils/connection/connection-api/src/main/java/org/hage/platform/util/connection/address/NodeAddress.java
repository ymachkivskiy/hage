package org.hage.platform.util.connection.address;

import java.io.Serializable;


public interface NodeAddress extends Serializable, Comparable<NodeAddress> {
    String getUniqueIdentifier();
}
