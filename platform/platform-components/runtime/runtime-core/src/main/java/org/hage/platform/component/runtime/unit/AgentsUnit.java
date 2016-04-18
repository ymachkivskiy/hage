package org.hage.platform.component.runtime.unit;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.execution.ExecutionUnit;
import org.hage.platform.component.runtime.init.UnitPopulation;
import org.hage.platform.component.runtime.unit.context.AgentLocalEnvironment;
import org.hage.platform.component.runtime.unit.context.ContextAdapter;
import org.hage.platform.component.runtime.unit.context.ControlContextAdapter;
import org.hage.platform.component.runtime.unit.location.UnitLocationContext;
import org.hage.platform.component.runtime.unit.population.AgentAdapter;
import org.hage.platform.component.runtime.unit.population.UnitAgentCreationContext;
import org.hage.platform.component.runtime.unit.population.UnitPopulationController;
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
    private UnitPopulationController unitPopulationController;

    private List<UnitStepCycleAware> stepCycleAwares = emptyList();

    private ControlContextAdapter controlAgentContext;
    private ContextAdapter agentContext;

    @Override
    public String getUniqueIdentifier() {
        return locationContext.getUniqueIdentifier();
    }

    @Override
    public void performControlAgentStep() {
        unitPopulationController.runControlAgent();
    }

    @Override
    public void performAgentsStep() {
        unitPopulationController.runAgents();
    }

    @Override
    public void afterStepPerformed() {
        log.debug("After step performed on unit on {}", locationContext);
        stepCycleAwares.forEach(UnitStepCycleAware::afterStepPerformed);
    }

    @Override
    public AgentManageContext contextForAgent(AgentAdapter agentAdapter) {
        log.debug("Get context for agent {}", agentAdapter);

        agentContext.setAgentAdapter(agentAdapter);
        return agentContext;
    }

    @Override
    public ControlAgentManageContext contextForControlAgent() {
        log.debug("Get context for control agent");

        return controlAgentContext;
    }

    public void setStepCycleAwares(List<UnitStepCycleAware> stepCycleAwares) {
        this.stepCycleAwares = nullSafeCopy(stepCycleAwares);
    }

    public void loadPopulation(UnitPopulation population) {
        unitAgentCreationContext.loadPopulation(population);
    }

    void initialize() {
        agentContext = new ContextAdapter(locationContext, unitAgentCreationContext, unitPopulationController);
        controlAgentContext = new ControlContextAdapter(locationContext, unitAgentCreationContext, unitPopulationController);
    }
}
