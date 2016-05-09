package org.hage.platform.component.runtime.container;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.container.MutableInstanceContainer;
import org.hage.platform.component.container.definition.IComponentDefinition;
import org.hage.platform.component.runtime.init.AgentDefinition;
import org.hage.platform.component.runtime.init.ContainerConfiguration;
import org.hage.platform.component.runtime.init.ContainerConfigurator;
import org.hage.platform.component.runtime.init.ControlAgentDefinition;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.control.ControlAgent;
import org.hage.platform.simulation.runtime.stopcondition.StopCondition;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

@SingletonComponent
@Slf4j
public class GlobalInstanceContainerController implements ContainerConfigurator, SimulationAgentDefinitionsSupplier, StopConditionSupplier {

    @Autowired
    private MutableInstanceContainer globalInstanceContainer;

    private final AtomicReference<Optional<Class<? extends ControlAgent>>> controlAgentType = new AtomicReference<>(empty());
    private final AtomicReference<Set<Class<? extends Agent>>> supportedAgentsTypes = new AtomicReference<>(emptySet());

    @Override
    public void setupConfiguration(ContainerConfiguration configuration) {
        log.debug("Setup container configuration {}", configuration);

        registerGlobalComponents(configuration.getGlobalComponents());
        registerControlAgentType(configuration.getControlAgentDefinition());
        registerAgentsTypes(configuration.getAgentDefinitions());
        registerStopCondition(configuration.getStopCondition());
    }

    @Override
    public boolean isSupportedAgent(Class<? extends Agent> agentClazz) {
        return getSupportedAgentTypes().contains(agentClazz);
    }

    @Override
    public Optional<Class<? extends ControlAgent>> getControlAgentType() {
        return controlAgentType.get();
    }

    @Override
    public Set<Class<? extends Agent>> getSupportedAgentTypes() {
        return supportedAgentsTypes.get();
    }

    @Override
    public Optional<StopCondition> getStopCondition() {
        return ofNullable(globalInstanceContainer.getInstance(StopCondition.class));
    }

    private void registerStopCondition(Class<? extends StopCondition> stopCondition) {
        Optional<Class<? extends StopCondition>> oStopCond = ofNullable(stopCondition);
        log.debug("Registering stop condition class {}", oStopCond);
        oStopCond.ifPresent(globalInstanceContainer::addSingletonComponent);
    }

    private void registerGlobalComponents(Collection<IComponentDefinition> globalComponents) {
        log.debug("Register global components {}", globalComponents);

        for (val component : globalComponents) {
            globalInstanceContainer.addComponent(component);
        }

        globalInstanceContainer.initializeStatefulComponents();
    }

    private void registerAgentsTypes(Collection<AgentDefinition> definitions) {
        log.debug("Register agent definitions {}", definitions);

        supportedAgentsTypes.set(unmodifiableSet(
            definitions.stream()
                .map(AgentDefinition::getAgentClass)
                .collect(toSet())));

        supportedAgentsTypes.get().stream().forEach(this::registerPrototypeComponent);
    }


    private void registerControlAgentType(ControlAgentDefinition definition) {
        log.debug("Register control agent definition {}", definition);

        controlAgentType.set(ofNullable(definition).map(ControlAgentDefinition::getClazz));
        controlAgentType.get().ifPresent(this::registerPrototypeComponent);
    }

    private void registerPrototypeComponent(Class<?> clazz) {
        log.debug("Register prototype component {}", clazz);
        globalInstanceContainer.addPrototypeComponent(clazz);
    }
}
