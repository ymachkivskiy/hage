package org.hage.platform.component.runtime.unit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.activepopulation.AgentAdapter;
import org.hage.platform.component.runtime.activepopulation.AgentsRunner;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.component.runtime.container.UnitAgentCreationController;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.populationinit.UnitPopulationLoader;
import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

import static lombok.AccessLevel.PACKAGE;


@Slf4j
@RequiredArgsConstructor
class AgentsUnit implements AgentExecutionContextEnvironment, AgentsExecutionUnit {

    @Getter
    private final Position position;

    @Setter(PACKAGE)
    private UnitAgentCreationController unitAgentCreationController;
    @Setter(PACKAGE)
    private UnitLocationController unitLocationController;
    @Setter(PACKAGE)
    private UnitActivePopulationController unitActivePopulationController;
    @Setter(PACKAGE)
    private AgentContextAdapter agentContextAdapter;

    @Override
    public String getUniqueIdentifier() {
        return unitLocationController.getUniqueIdentifier();
    }

    @Override
    public void performPostProcessing() {
        unitLocationController.performPostProcessing();
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

    @Override
    public UnitPopulationLoader getUnitPopulationLoader() {
        return unitAgentCreationController;
    }

}
