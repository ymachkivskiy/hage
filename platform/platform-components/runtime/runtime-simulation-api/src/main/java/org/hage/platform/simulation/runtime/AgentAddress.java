package org.hage.platform.simulation.runtime;


import java.io.Serializable;

public interface AgentAddress extends Serializable {
    String getUniqueIdentifier();

    String getFriendlyName();
}
