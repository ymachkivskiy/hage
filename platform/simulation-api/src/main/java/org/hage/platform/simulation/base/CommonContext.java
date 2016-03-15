package org.hage.platform.simulation.base;

import org.hage.platform.simulation.identification.UnitAddress;

import java.util.Set;

public interface CommonContext {

    UnitAddress queryUnit();

    Set<UnitAddress> querySurroundingUnits();

    void notifyStopConditionSatisfied();

}
