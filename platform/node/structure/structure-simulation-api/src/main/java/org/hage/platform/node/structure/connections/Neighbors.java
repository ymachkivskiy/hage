package org.hage.platform.node.structure.connections;


import java.util.List;

public interface Neighbors {

    List<UnitAddress> getLocated(RelativePosition firstRelativeSelector, RelativePosition... otherSelectors);

    List<UnitAddress> getAll();

}
