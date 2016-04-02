package org.hage.platform.component.structure.distribution;


import org.hage.platform.component.structure.Position;

import java.util.Collection;
import java.util.List;

public interface LocalPositionsController {
    void activate(Collection<Position> positions);

    void activate(Position position);

    void deactivate(List<Position> positions);

}
