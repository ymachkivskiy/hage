package org.hage.platform.component.structure.connections;

import java.util.List;
import java.util.Set;

public interface Neighborhood {

    List<Position> getNeighborsFor(RelativePosition relativePosition);

    Set<Position> getAllNeighbors();

}
