package org.hage.platform.simulation.identification;

import java.io.Serializable;

public interface Address extends Serializable {
    String getUniqueIdentifier();

    String getFriendlyName();
}
