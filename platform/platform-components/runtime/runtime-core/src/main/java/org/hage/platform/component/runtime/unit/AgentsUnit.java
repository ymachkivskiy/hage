package org.hage.platform.component.runtime.unit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.activepopulation.AgentAdapter;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.component.runtime.container.UnitAgentCreationController;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.unit.context.AgentContextAdapter;
import org.hage.platform.component.runtime.unit.context.AgentExecutionContextEnvironment;
import org.hage.platform.component.structure.Position;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

import java.util.Collection;

import static lombok.AccessLevel.PACKAGE;
import static org.hage.util.ObjectUtils.allNotNull;


@Slf4j
@RequiredArgsConstructor
@ToString(doNotUseGetters = true, of = "position")
class AgentsUnit implements AgentExecutionContextEnvironment, Unit {

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
    public void postProcess() {
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
    public void addAgentsImmediately(Collection<? extends Agent> agents) {
        unitActivePopulationController.addAgentsImmediately(agents);
    }

    @Override
    public void runAgents() {
        unitActivePopulationController.runAgents();
    }

    @Override
    public void runControlAgent() {
        unitActivePopulationController.runControlAgent();
    }

    @Override
    public void loadPopulation(UnitPopulation population) {
        unitAgentCreationController.loadPopulation(population);
    }

    boolean isInitialized() {
        return allNotNull(
            unitActivePopulationController,
            unitLocationController,
            unitAgentCreationController,
            agentContextAdapter);
    }
}
