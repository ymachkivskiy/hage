package org.hage.platform.component.simulationconfig.load.generate;

import org.hage.platform.component.runtime.init.AgentDefinition;
import org.hage.platform.component.runtime.init.Population;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.component.simulationconfig.load.definition.ChunkPopulationQualifier;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.grid.Chunk;
import org.hage.platform.component.simulationconfig.load.definition.agent.AgentCountData;
import org.hage.platform.component.simulationconfig.load.definition.agent.ChunkAgentDistribution;
import org.hage.platform.component.simulationconfig.load.generate.count.AgentCountProvider;
import org.hage.platform.component.simulationconfig.load.generate.select.PositionsSelector;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class MergingPopulationDistributionMapCreator implements PopulationDistributionMapCreator {

    private PositionsSelector positionsSelector;
    private AgentCountProvider countProvider;

    @Override
    public Population createMap(ChunkPopulationQualifier populationQualifier) {
        return populationQualifier.getChunkAgentDistributions()
            .stream()
            .map(agentDistribution -> createDistributionMapFor(populationQualifier.getChunk(), agentDistribution))
            .reduce(Population.emptyPopulation(), Population::merge);
    }

    private Population createDistributionMapFor(Chunk chunk, ChunkAgentDistribution agentDistribution) {
        Set<Position> selectedPositions = getSelectedInternalPositions(chunk, agentDistribution);
        return Population.populationFromMap(createPopulationMapForSelectedPositions(selectedPositions, agentDistribution));
    }

    private Set<Position> getSelectedInternalPositions(Chunk chunk, ChunkAgentDistribution agentDistribution) {
        return positionsSelector.select(chunk, agentDistribution.getPositionsSelectionData());
    }

    private Map<Position, UnitPopulation> createPopulationMapForSelectedPositions(Collection<Position> selectedPositions, ChunkAgentDistribution agentDistribution) {
        AgentDefinition agentDef = agentDistribution.getAgentDefinition();
        AgentCountData countData = agentDistribution.getCountData();

        return selectedPositions
            .stream()
            .collect(toMap(
                (x) -> x,
                (x) -> UnitPopulation.populationFromPair(agentDef, countProvider.getAgentCount(countData))
            ));
    }

    @Required
    public void setPositionsSelector(PositionsSelector positionsSelector) {
        this.positionsSelector = positionsSelector;
    }

    @Required
    public void setCountProvider(AgentCountProvider countProvider) {
        this.countProvider = countProvider;
    }
}
