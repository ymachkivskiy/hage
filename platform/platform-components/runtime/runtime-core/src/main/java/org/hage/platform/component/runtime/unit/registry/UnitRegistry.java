package org.hage.platform.component.runtime.unit.registry;

import org.hage.platform.component.runtime.unit.Unit;
import org.hage.platform.component.structure.Position;

public interface UnitRegistry {
    Unit unitFor(Position position);
}
