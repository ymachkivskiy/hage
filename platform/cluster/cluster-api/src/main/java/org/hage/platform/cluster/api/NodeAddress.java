package org.hage.platform.cluster.api;

import java.io.Serializable;


public interface NodeAddress extends Serializable {
    String getFriendlyIdentifier();
}
