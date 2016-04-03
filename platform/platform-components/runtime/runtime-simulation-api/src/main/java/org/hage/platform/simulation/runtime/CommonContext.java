package org.hage.platform.simulation.runtime;


import java.util.Set;

public interface CommonContext {

    UnitAddress queryUnit();

    // TODO: use neighborhood
    Set<UnitAddress> querySurroundingUnits();

    void notifyStopConditionSatisfied();

}
