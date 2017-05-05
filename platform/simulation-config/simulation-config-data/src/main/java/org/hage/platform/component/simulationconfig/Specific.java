package org.hage.platform.component.simulationconfig;

import lombok.Data;
import org.hage.platform.node.runtime.init.Population;

import java.io.Serializable;

@Data
public class Specific implements Serializable {
    private final Population population;
}
