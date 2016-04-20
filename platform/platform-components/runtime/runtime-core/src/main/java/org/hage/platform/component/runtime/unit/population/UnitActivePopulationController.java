package org.hage.platform.component.runtime.unit.population;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.component.runtime.unit.UnitStepCycleAware;
import org.hage.platform.component.runtime.unit.agentcontext.AgentLocalEnvironment;
import org.hage.platform.component.runtime.util.StatefulFinisher;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.control.ControlAgent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

@Slf4j
@PrototypeComponent
public class UnitActivePopulationController implements UnitStepCycleAware {

    private final AgentLocalEnvironment agentEnvironment;
    private final AtomicLong agentIdCounter = new AtomicLong();

    private final List<Agent> disposedAgents = new LinkedList<>();

    private final List<AgentAdapter> toBeRemoved = new LinkedList<>();
    private final List<AgentAdapter> toBeAdded = new LinkedList<>();
    private final Set<AgentAdapter> agentsAdapters = new HashSet<>();

    private Optional<ControlAgentAdapter> controlAgent = empty();

    @Autowired
    private StatefulFinisher statefulFinisher;

    public UnitActivePopulationController(AgentLocalEnvironment environment) {
        this.agentEnvironment = environment;
    }

    @Override
    public void afterStepPerformed() {
        flushAgents();
        finishDisposedAgents();
    }

    public void runControlAgent() {
        log.debug("Run control agent");
        controlAgent.ifPresent(ControlAgentAdapter::performStep);
    }

    public void runAgents() {
        log.debug("Run all agents");
        agentsAdapters.forEach(AgentAdapter::performStep);
    }

    void setControlAgent(ControlAgent controlAgent) {
        log.debug("Set control agent to {}", controlAgent);
        this.controlAgent = of(new ControlAgentAdapter(agentEnvironment, controlAgent));
    }

    void addInstancesImmediately(Collection<? extends Agent> agents) {
        log.debug("Add agents into local population {}", agents);
        agents.stream()
            .map(this::createAdapterFor)
            .forEach(agentsAdapters::add);
    }

    public void scheduleAddInstances(Collection<? extends Agent> agents) {
        agents.stream()
            .map(this::createAdapterFor)
            .forEach(toBeAdded::add);
    }

    public boolean remove(AgentAdapter agentAdapter) {
        log.debug("Remove agent {} immediately", agentAdapter.getFriendlyIdentifier());
        return agentsAdapters.remove(agentAdapter);
    }

    public boolean removeWithKilling(AgentAdapter agentAdapter) {
        boolean result = remove(agentAdapter);
        if (result) {
            disposedAgents.add(agentAdapter.getAgent());
        }
        return result;
    }

    public void scheduleRemove(Collection<AgentAdapter> agentAdapters) {
        log.debug("Insert agents to be removed {}", agentAdapters);
        toBeRemoved.addAll(agentAdapters);
    }

    public void scheduleRemoveWithKilling(Collection<AgentAdapter> agentAdapters) {
        log.debug("Remove agents {} with killing", agentAdapters);

        scheduleRemove(agentAdapters);
        agentAdapters.stream()
            .map(AgentAdapter::getAgent)
            .forEach(disposedAgents::add);
    }

    public void scheduleRemoveWithKilling(AgentAdapter agentAdapter) {
        log.debug("Remove agent {} with killing", agentAdapter);

        toBeRemoved.add(agentAdapter);
        disposedAgents.add(agentAdapter.getAgent());
    }

    public <T extends Agent> List<AgentAdapter> getAdaptersForAgentsOfType(Class<? extends Agent> agentClazz) {
        return agentsAdapters.stream()
            .filter(agentAdapter -> agentAdapter.getAgent().getClass().equals(agentClazz))
            .collect(toList());
    }

    public Set<AgentAdapter> getAllAdapters() {
        return unmodifiableSet(agentsAdapters);
    }

    private void flushAgents() {
        log.debug("Append all created during step agents {}", toBeAdded);
        log.debug("Remove all killed and dead agents {}", toBeRemoved);

        agentsAdapters.removeAll(toBeRemoved);
        agentsAdapters.addAll(toBeAdded);
        toBeRemoved.clear();
        toBeAdded.clear();
    }

    private void finishDisposedAgents() {
        log.debug("Finish {} disposed agents", disposedAgents.size());

        statefulFinisher.finish(disposedAgents);
        disposedAgents.clear();
    }

    private AgentAdapter createAdapterFor(Agent agent) {
        return new AgentAdapter(agent, agentIdCounter.getAndIncrement(), agentEnvironment);
    }

}
