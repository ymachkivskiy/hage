package org.hage.platform.component.runtime.unit;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.execution.ExecutionUnit;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.component.runtime.unit.agentcontext.AgentLocalEnvironment;
import org.hage.platform.component.runtime.unit.agentcontext.UnitAgentContextAdapter;
import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.AgentAdapter;
import org.hage.platform.component.runtime.unit.population.UnitAgentCreationContext;
import org.hage.platform.component.runtime.unit.population.UnitActivePopulationController;
import org.hage.platform.simulation.runtime.agent.AgentManageContext;
import org.hage.platform.simulation.runtime.control.ControlAgentManageContext;

import java.util.List;

import static java.util.Collections.emptyList;
import static lombok.AccessLevel.PACKAGE;
import static org.hage.util.CollectionUtils.nullSafeCopy;


@Slf4j
public class AgentsUnit implements ExecutionUnit, AgentLocalEnvironment {

    @Setter(PACKAGE)
    private UnitAgentCreationContext unitAgentCreationContext;
    @Setter(PACKAGE)
    private UnitLocationContext locationContext;
    @Setter(PACKAGE)
    private UnitActivePopulationController unitActivePopulationController;
    @Setter(PACKAGE)
    private UnitAgentContextAdapter agentContextAdapter;

    private List<UnitStepCycleAware> stepCycleAwares = emptyList();

    @Override
    public String getUniqueIdentifier() {
        return locationContext.getUniqueIdentifier();
    }

    @Override
    public void performControlAgentStep() {
        unitActivePopulationController.runControlAgent();
    }

    @Override
    public void performAgentsStep() {
        unitActivePopulationController.runAgents();
    }

    @Override
    public void afterStepPerformed() {
        log.debug("After step performed on unit on {}", locationContext);
        stepCycleAwares.forEach(UnitStepCycleAware::afterStepPerformed);
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

    public void setStepCycleAwares(List<UnitStepCycleAware> stepCycleAwares) {
        this.stepCycleAwares = nullSafeCopy(stepCycleAwares);
    }

    public void loadPopulation(UnitPopulation population) {
        unitAgentCreationContext.loadPopulation(population);
    }

}
