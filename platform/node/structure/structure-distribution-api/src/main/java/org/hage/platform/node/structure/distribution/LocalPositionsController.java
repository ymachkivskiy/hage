package org.hage.platform.node.structure.distribution;


import org.hage.platform.node.structure.Position;

public interface LocalPositionsController {

    void activateLocally(Position position);

    void deactivateLocally(Position position);

}
