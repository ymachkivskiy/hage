package org.hage.platform.component.monitoring;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data // TODO: change
public class SummaryAgentsStats implements Serializable {
    private final long summaryAgentsNumber;
    private final List<UnitSpecificAgentsStats> unitSpecificAgentsStats;

    public int getUnitsNumber() {
        return unitSpecificAgentsStats.size();
    }
}
