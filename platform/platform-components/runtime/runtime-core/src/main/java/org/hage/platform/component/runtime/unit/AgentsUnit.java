package org.hage.platform.component.runtime.unit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.component.runtime.unit.agentcontext.AgentLocalEnvironment;
import org.hage.platform.component.runtime.unit.agentcontext.UnitAgentContextAdapter;
import org.hage.platform.component.runtime.unit.api.AgentsExecutionUnit;
import org.hage.platform.component.runtime.unit.api.AgentsRunner;
import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.AgentAdapter;
import org.hage.platform.component.runtime.unit.population.UnitActivePopulationController;
import org.hage.platform.component.runtime.unit.population.UnitAgentCreationContext;
import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

import static lombok.AccessLevel.PACKAGE;
import static org.hage.util.CollectionUtils.nullSafeCopy;


@Slf4j
@RequiredArgsConstructor
public class AgentsUnit implements AgentLocalEnvironment, AgentsExecutionUnit {

    @Getter
    private final Position position;

    @Setter(PACKAGE)
    private UnitAgentCreationContext unitAgentCreationContext;
    @Setter(PACKAGE)
    private UnitLocationContext locationContext;
    @Setter(PACKAGE)
    private UnitActivePopulationController unitActivePopulationController;
    @Setter(PACKAGE)
    private UnitAgentContextAdapter agentContextAdapter;

    @Override
    public String getUniqueIdentifier() {
        return locationContext.getUniqueIdentifier();
    }

    @Override
    public void performPostProcessing() {
        locationContext.performPostProcessing();
        unitActivePopulationController.performPostProcessing();
    }

    @Override
    public AgentManageContext contextForAgent(AgentAdapter agentAdapter) {
        log.debug("Get context for agent {}", agentAdapter);
        agentContextAdapter.setCurrentAgentContext(agentAdapter);
        return agentContextAdapter;
    }

    @Override
    public ControlAgentManageContext contextForControlAgent() {
        log.debug("Get context for control agent");
        agentContextAdapter.setControlAgentContext();
        return agentContextAdapter;
    }

    @Override
    public AgentsRunner getAgentsRunner() {
        return unitActivePopulationController;
    }

    public void loadPopulation(UnitPopulation population) {
        unitAgentCreationContext.loadPopulation(population);
    }

}
