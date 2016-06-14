package org.hage.platform.component.loadbalance.knapsack.util;

import org.hage.platform.component.execution.monitor.UnitAgentsNumberInfo;

public abstract class CalculationUtils {

    public static boolean validForComputation(UnitAgentsNumberInfo unitAgentsNumberInfo) {
        return rateAgentsInfo(unitAgentsNumberInfo) > 0;
    }

    public static int rateAgentsInfo(UnitAgentsNumberInfo unitAgentsNumberInfo) {
        return unitAgentsNumberInfo.getAgentsInfo().getActiveAgentsNumber();
    }
}
