package org.hage.platform.node.runtime.unit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.node.execution.monitor.AgentsInfo;
import org.hage.platform.node.runtime.activepopulation.AgentAdapter;
import org.hage.platform.node.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.node.runtime.container.UnitComponentCreationController;
import org.hage.platform.node.runtime.init.UnitPopulation;
import org.hage.platform.node.runtime.location.UnitLocationController;
import org.hage.platform.node.runtime.stateprops.UnitPropertiesController;
import org.hage.platform.node.runtime.unitmove.PackedUnit;
import org.hage.platform.node.runtime.unitmove.UnitConfiguration;
import org.hage.platform.node.structure.Position;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.control.ControlAgent;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;
import org.hage.platform.simulation.runtime.state.UnitPropertiesUpdater;

import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PACKAGE;
import static org.hage.util.ObjectUtils.allNotNull;


@Slf4j
@RequiredArgsConstructor
@ToString(doNotUseGetters = true, of = "position")
public class AgentsUnit implements AgentExecutionContextEnvironment, Unit {

    @Getter
    private final Position position;

    @Setter(PACKAGE)
    private UnitComponentCreationController unitComponentCreationController;
    @Setter(PACKAGE)
    private UnitLocationController unitLocationController;
    @Setter(PACKAGE)
    private UnitActivePopulationController unitActivePopulationController;
    @Setter(PACKAGE)
    private AgentContextAdapter agentContextAdapter;
    @Setter(PACKAGE)
    private UnitPropertiesController unitPropertiesController;

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
    public AgentsInfo getInfo() {
        return unitActivePopulationController.getInfo();
    }

    @Override
    public void loadPopulation(UnitPopulation population) {
        unitComponentCreationController.loadPopulation(population);
    }

    @Override
    public void performStateChange() {
        unitPropertiesController.performStateChange();
    }

    PackedUnit pack() {
        log.debug("Pack unit on position {}", position);

        List<Agent> agents = unitActivePopulationController.serializeAgents();
        ControlAgent controlAgent = unitActivePopulationController.serializeControlAgent().orElse(null);
        UnitPropertiesUpdater unitPropertiesUpdater = unitPropertiesController.serializeUnitPropertiesUpdater().orElse(null);

        return new PackedUnit(position, new UnitConfiguration(controlAgent, unitPropertiesUpdater, agents));
    }

    boolean isInitialized() {
        return allNotNull(
            unitActivePopulationController,
            unitLocationController,
            unitComponentCreationController,
            agentContextAdapter,
            unitPropertiesController);
    }
}
