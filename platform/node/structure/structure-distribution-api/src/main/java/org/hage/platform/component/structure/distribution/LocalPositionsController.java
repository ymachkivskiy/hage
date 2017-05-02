package org.hage.platform.component.structure.distribution;


import org.hage.platform.component.structure.Position;

public interface LocalPositionsController {

    void activateLocally(Position position);

    void deactivateLocally(Position position);

}
