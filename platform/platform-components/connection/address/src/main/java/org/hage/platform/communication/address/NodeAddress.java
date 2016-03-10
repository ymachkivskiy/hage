package org.hage.platform.communication.address;

import java.io.Serializable;


public interface NodeAddress extends Serializable {
    String getUniqueIdentifier();
}
