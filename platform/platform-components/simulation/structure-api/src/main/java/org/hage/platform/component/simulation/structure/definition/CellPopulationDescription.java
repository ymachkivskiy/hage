package org.hage.platform.component.simulation.structure.definition;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hage.platform.component.simulation.agent.Agent;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.*;

@Immutable
@EqualsAndHashCode
@ToString
public class CellPopulationDescription implements Serializable {
    private static final CellPopulationDescription EMPTY = new CellPopulationDescription(emptyMap());
    private final Map<Agent, Integer> agentCountMap;

    private CellPopulationDescription(Map<Agent, Integer> agentCountMap) {
        this.agentCountMap = unmodifiableMap(agentCountMap);
    }

    public static CellPopulationDescription emptyPopulation() {
        return EMPTY;
    }

    public static CellPopulationDescription populationFromMap(Map<Agent, Integer> agentCountMap) {
        return new CellPopulationDescription(agentCountMap);
    }

    public static CellPopulationDescription populationFromPair(Agent agentDefinition, Integer agentCount) {
        return new CellPopulationDescription(singletonMap(agentDefinition, agentCount));
    }

    public List<Agent> getAgents() {
        return new ArrayList<>(agentCountMap.keySet());
    }

    public int getAgentCountForDefinition(Agent definition) {
        return agentCountMap.getOrDefault(definition, 0);
    }

    public int getAgentsCount() {
        return agentCountMap.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    public CellPopulationDescription merge(CellPopulationDescription other) {
        if (this.getAgents().isEmpty()) {
            return other;
        }
        if (other.getAgents().isEmpty()) {
            return this;
        }

        Map<Agent, Integer> mergingMap = new HashMap<>(agentCountMap);

        for (Agent agentDefinition : other.getAgents()) {
            int count = other.getAgentCountForDefinition(agentDefinition);
            Integer currentCount = mergingMap.getOrDefault(agentDefinition, 0);
            mergingMap.put(agentDefinition, currentCount + count);
        }

        return populationFromMap(mergingMap);
    }

    public boolean isEmpty() {
        return agentCountMap.isEmpty();
    }
}
