package org.hage.platform.config.def;

import lombok.EqualsAndHashCode;
import org.hage.platform.habitat.AgentDefinition;

import javax.annotation.concurrent.Immutable;
import java.util.*;

import static java.util.Collections.emptyMap;

@Immutable
@EqualsAndHashCode
public class CellPopulationDescription {
    private static final CellPopulationDescription EMPTY = new CellPopulationDescription(emptyMap());

    private final Map<AgentDefinition, Integer> agentCountMap;

    private CellPopulationDescription(Map<AgentDefinition, Integer> agentCountMap) {
        this.agentCountMap = Collections.unmodifiableMap(agentCountMap);
    }

    public static CellPopulationDescription empty() {
        return EMPTY;
    }

    public static CellPopulationDescription fromMap(Map<AgentDefinition, Integer> agentCountMap) {
        return new CellPopulationDescription(agentCountMap);
    }

    public List<AgentDefinition> getAgentDefinitions() {
        return new ArrayList<>(agentCountMap.keySet());
    }

    public int getAgentCountForDefinition(AgentDefinition definition) {
        return agentCountMap.getOrDefault(definition, 0);
    }

    public int getAgentsCount() {
        return agentCountMap.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    public CellPopulationDescription merge(CellPopulationDescription other) {
        return EMPTY;
    }

    public boolean isEmpty() {
        return agentCountMap.isEmpty();
    }
}
