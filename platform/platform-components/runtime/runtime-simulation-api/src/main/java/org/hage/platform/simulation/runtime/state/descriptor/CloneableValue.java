package org.hage.platform.simulation.runtime.state.descriptor;

import java.io.Serializable;

public interface CloneableValue<T extends Serializable> extends Serializable {
    T createClone();
}
