package org.hage.platform.simulation.runtime;


import java.util.Set;

public interface CommonContext {

    UnitAddress queryUnit();

    Set<UnitAddress> querySurroundingUnits();

    void notifyStopConditionSatisfied();

}
