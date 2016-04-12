package org.hage.platform.component.structure.connections;


import java.util.List;

public interface Neighbors {

    List<UnitAddress> getLocated(RelativePosition firstRelativeSelector, RelativePosition... otherSelectors);

    List<UnitAddress> getAll();

}
