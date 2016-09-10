package org.hage.platform.component.runtime.location;

import org.hage.platform.component.structure.Position;

public interface UnitLocationControllerFactory {
    UnitLocationController createForPosition(Position position);
}
