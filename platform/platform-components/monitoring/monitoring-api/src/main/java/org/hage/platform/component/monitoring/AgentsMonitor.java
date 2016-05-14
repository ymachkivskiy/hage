package org.hage.platform.component.monitoring;

import java.util.List;

public interface AgentsMonitor {
    SummaryAgentsStats getSummaryAgentsStats();

    List<UnitAgentsStats> getUnitAgentsStats();
}
