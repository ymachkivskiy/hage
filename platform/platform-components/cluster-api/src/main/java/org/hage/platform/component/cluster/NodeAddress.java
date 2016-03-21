package org.hage.platform.component.cluster;

import java.io.Serializable;


public interface NodeAddress extends Serializable {
    String getUniqueIdentifier();
}
