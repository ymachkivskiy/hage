package org.hage.platform.node.runtime.unit.registry;

import org.hage.platform.node.runtime.unit.Unit;
import org.hage.platform.node.structure.Position;

import java.util.Collection;

public interface UnitRegistry {
    Unit unitFor(Position position);

    Collection<? extends Unit> getAllUnits();
}
