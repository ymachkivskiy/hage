package org.hage.platform.node.runtime.unit;

import org.hage.platform.annotation.di.PrototypeComponent;
import org.hage.platform.node.container.InstanceContainer;
import org.hage.platform.node.container.MutableInstanceContainer;
import org.hage.platform.node.runtime.container.SimulationAgentDefinitionsSupplier;
import org.hage.platform.node.runtime.container.dependency.DependenciesEraser;
import org.hage.platform.node.runtime.container.dependency.DependenciesInjector;
import org.hage.platform.node.runtime.container.dependency.LocalDependenciesInjector;
import org.hage.platform.simulation.runtime.agent.Agent;
import org.hage.platform.simulation.runtime.control.ControlAgent;
import org.hage.platform.simulation.runtime.state.UnitPropertiesUpdater;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@PrototypeComponent
public class UnitContainer implements LocalDependenciesInjector {

    private InstanceContainer unitInstanceContainer;

    @Autowired
    private DependenciesEraser dependenciesEraser;
    @Autowired
    private DependenciesInjector dependenciesInjector;
    @Autowired
    private SimulationAgentDefinitionsSupplier agentDefinitionsSupplier;


    @Override
    public void injectDependencies(Object object) {
        dependenciesInjector.injectDependenciesUsing(object, unitInstanceContainer);
    }

    public void eraseDependencies(Object object) {
        dependenciesEraser.eraseDependencies(object);
    }

    public Optional<ControlAgent> getControlAgentInstance() {
        return agentDefinitionsSupplier.getControlAgentType()
            .map(unitInstanceContainer::getInstance);
    }

    public <T extends Agent> T newAgentInstance(Class<T> agentType) {
        return unitInstanceContainer.getInstance(agentType);
    }

    public Optional<UnitPropertiesUpdater> newUnitPropertiesUpdaterInstance() {
        return ofNullable(unitInstanceContainer.getInstance(UnitPropertiesUpdater.class));
    }

    @Autowired
    private void createInstanceContainerUsing(MutableInstanceContainer mutableInstanceContainer) {
        unitInstanceContainer = mutableInstanceContainer.newChildContainer();
    }

}
