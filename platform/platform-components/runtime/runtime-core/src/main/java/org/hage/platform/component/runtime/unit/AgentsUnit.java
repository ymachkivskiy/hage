package org.hage.platform.component.runtime.unit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.execution.ExecutionUnit;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.component.runtime.init.UnitPopulationInitializer;
import org.hage.platform.component.runtime.unit.agent.AgentAdapter;
import org.hage.platform.component.runtime.unit.agent.ControlAgentAdapter;
import org.hage.platform.component.runtime.unit.population.InitialPopulation;
import org.hage.platform.component.runtime.unit.population.UnitPopulationController;
import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.Agent;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;


// TODO: think about spliting functionality from actual population inside unit
@Slf4j
@RequiredArgsConstructor
public class AgentsUnit implements ExecutionUnit, UnitPopulationInitializer {

    private final Position position;
    private final UnitPopulationController populationController;

    private List<AgentAdapter> agentAdapters = emptyList();
    private Optional<ControlAgentAdapter> controlAgent = empty();


    @Override
    public String getUnitId() {
        return position.toString();
    }

    @Override
    public void performControlAgentStep() {
        controlAgent.ifPresent(ControlAgentAdapter::performStep);
    }

    @Override
    public void performAgentsStep() {
        agentAdapters.forEach(AgentAdapter::performStep);
    }

    @Override
    public void initializeWith(UnitPopulation population) {
        InitialPopulation initialPopulation = populationController.configureWithUnitPopulation(population);

        agentAdapters = initialPopulation.getAgents().stream()
            .map(this::createAdapter)
            .collect(toList());

    }

    private AgentAdapter createAdapter(Agent agent) {
        // TODO: not implemented
        return new AgentAdapter(1, "name", agent);
    }
}
