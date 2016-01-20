package org.hage.platform.config.transl;

import org.hage.platform.config.def.CellPopulationDescription;
import org.hage.platform.config.def.ChunkPopulationQualifier;
import org.hage.platform.config.def.PopulationDistributionMap;
import org.hage.platform.config.def.agent.AgentCountData;
import org.hage.platform.config.def.agent.ChunkAgentDistribution;
import org.hage.platform.config.transl.count.AgentCountProvider;
import org.hage.platform.config.transl.select.PositionsSelector;
import org.hage.platform.habitat.AgentDefinition;
import org.hage.platform.habitat.structure.Chunk;
import org.hage.platform.habitat.structure.InternalPosition;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static org.hage.platform.config.def.CellPopulationDescription.populationFromPair;
import static org.hage.platform.config.def.PopulationDistributionMap.distributionFromMap;

class MergingPopulationDistributionMapCreator implements PopulationDistributionMapCreator {

    private PositionsSelector positionsSelector;
    private AgentCountProvider countProvider;

    @Override
    public PopulationDistributionMap createMap(ChunkPopulationQualifier populationQualifier) {
        return populationQualifier.getChunkAgentDistributions()
            .stream()
            .map(agentDistribution -> createDistributionMapFor(populationQualifier.getChunk(), agentDistribution))
            .reduce(PopulationDistributionMap.emptyDistributionMap(), PopulationDistributionMap::merge);
    }

    private PopulationDistributionMap createDistributionMapFor(Chunk chunk, ChunkAgentDistribution agentDistribution) {
        Set<InternalPosition> selectedInternalPositions = getSelectedInternalPositions(chunk, agentDistribution);
        return distributionFromMap(createPopulationMapForSelectedPositions(selectedInternalPositions, agentDistribution));
    }

    private Set<InternalPosition> getSelectedInternalPositions(Chunk chunk, ChunkAgentDistribution agentDistribution) {
        return positionsSelector.select(chunk, agentDistribution.getPositionsSelectionData());
    }

    private Map<InternalPosition, CellPopulationDescription> createPopulationMapForSelectedPositions(Collection<InternalPosition> selectedInternalPositions, ChunkAgentDistribution agentDistribution) {
        AgentDefinition agentDef = agentDistribution.getAgent();
        AgentCountData countData = agentDistribution.getCountData();

        return selectedInternalPositions
            .stream()
            .collect(toMap(
                (x) -> x,
                (x) -> populationFromPair(agentDef, countProvider.getAgentCount(countData))
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
