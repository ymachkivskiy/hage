package org.hage.platform.cluster.loadbalance.knapsack.util;

import org.hage.platform.component.execution.monitor.AgentsInfo;
import org.hage.platform.component.execution.monitor.UnitAgentsNumberInfo;

public abstract class CalculationUtils {

    public static boolean validForComputation(UnitAgentsNumberInfo unitAgentsNumberInfo) {
        return measureAgentsInfo(unitAgentsNumberInfo) > 0;
    }

    public static int rateAgentsInfo(UnitAgentsNumberInfo unitAgentsNumberInfo) {
        return unitAgentsNumberInfo.getAgentsInfo().getActiveAgentsNumber();
    }

    public static int measureAgentsInfo(UnitAgentsNumberInfo unitAgentsNumberInfo) {
        AgentsInfo agentsInfo = unitAgentsNumberInfo.getAgentsInfo();
        return agentsInfo.getActiveAgentsNumber() - agentsInfo.getAgentsToRemoveNumber() + agentsInfo.getAgentsToAddNumber();
    }

}
