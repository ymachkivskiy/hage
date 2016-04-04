package org.hage.platform.component.structure.distribution;


import org.hage.platform.component.structure.Position;

import java.util.Collection;
import java.util.List;

public interface LocalPositionsController {
    void activateLocally(Collection<Position> positions);

    void activateLocally(Position position);

    void deactivateLocally(List<Position> positions);

}
