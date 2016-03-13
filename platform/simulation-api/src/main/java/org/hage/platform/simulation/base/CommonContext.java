package org.hage.platform.simulation.base;

import org.hage.platform.simulation.identification.CellAddress;

import java.util.Set;

public interface CommonContext {

    CellAddress queryCellAddress();

    Set<CellAddress> querySurroundingCellAddresses();

    void notifyStopConditionSatisfied();

}
