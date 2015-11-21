package org.hage.platform.config.def;

import org.hage.platform.habitat.AgentDefinition;

import java.util.Map;

public class CellInternalPopulation {
    private Map<AgentDefinition, Integer> agentCountMap;

    public int getAgentsCount() {
        return agentCountMap.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }
}
