package org.hage.platform.component.runtime.unit.registry;

import org.hage.platform.component.runtime.unit.Unit;
import org.hage.platform.component.structure.Position;

import java.util.Collection;

public interface UnitRegistry {
    Unit unitFor(Position position);

    Collection<? extends Unit> getAllUnits();
}
