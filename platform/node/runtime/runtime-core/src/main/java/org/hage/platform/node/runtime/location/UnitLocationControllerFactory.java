package org.hage.platform.node.runtime.location;

import org.hage.platform.node.structure.Position;

public interface UnitLocationControllerFactory {
    UnitLocationController createForPosition(Position position);
}
