package org.hage.platform.component.runtime.unit.population;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.unit.AgentsUnit;
import org.hage.platform.component.runtime.unit.UnitStepCycleAware;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.control.ControlAgent;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
@RequiredArgsConstructor
public class PopulationController implements UnitStepCycleAware {

    private final AtomicLong agentIdCounter = new AtomicLong();
    private final AgentsUnit agentsUnit;

    private List<AgentAdapter> stepBuffer = new LinkedList<>();
    private Set<AgentAdapter> agentsAdapters = new HashSet<>();
    private Optional<ControlAgentAdapter> controlAgent = empty();

    public void runControlAgent() {
        log.debug("Run control agent");
        controlAgent.ifPresent(ControlAgentAdapter::performStep);
    }

    public void runAgents() {
        log.debug("Run all agents");
        agentsAdapters.forEach(AgentAdapter::performStep);
    }

    @Override
    public void afterStepPerformed() {
        log.debug("Append all created during step agents {}", stepBuffer);

        agentsAdapters.addAll(stepBuffer);
        stepBuffer = new LinkedList<>();
    }

    void setControlAgent(ControlAgent controlAgent) {
        log.debug("Set control agent to {}", controlAgent);
        this.controlAgent = of(new ControlAgentAdapter(agentsUnit, controlAgent));
    }

    void addAgent(Agent agent) {
        log.debug("Add agent into population of {}", agentsUnit.getUniqueIdentifier());
        agentsAdapters.add(createAdapterFor(agent));
    }

    void addAgents(Collection<? extends Agent> agents) {
        log.debug("Add agents into local population {}", agents);
        agents.stream()
            .map(this::createAdapterFor)
            .forEach(agentsAdapters::add);
    }

    void addAgentsToStepBuffer(Collection<? extends Agent> agents) {
        log.debug("Add agents to step buffer {}", agents);
        agents.stream()
            .map(this::createAdapterFor)
            .forEach(stepBuffer::add);
    }

    private AgentAdapter createAdapterFor(Agent agent) {
        return new AgentAdapter(agentsUnit, agentIdCounter.getAndIncrement(), agent);
    }

}
