package org.hage.platform.component.structure.connections;


import org.hage.platform.simulation.runtime.UnitAddress;

import java.util.List;

public interface Neighbors {

    List<UnitAddress> choose(RelativeSelector selector);

}
