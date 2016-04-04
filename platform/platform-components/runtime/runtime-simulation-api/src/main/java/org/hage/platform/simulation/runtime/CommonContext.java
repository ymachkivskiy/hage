package org.hage.platform.simulation.runtime;


import org.hage.platform.component.structure.connections.Neighbors;
import org.hage.platform.component.structure.connections.UnitAddress;

public interface CommonContext {

    UnitAddress queryUnit();

    Neighbors querySurroundingUnits();

    void notifyStopConditionSatisfied();

}
