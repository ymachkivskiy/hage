package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationControllerFactory;
import org.hage.platform.component.runtime.container.UnitComponentCreationController;
import org.hage.platform.component.runtime.container.UnitComponentCreationControllerFactory;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.location.UnitLocationControllerFactory;
import org.hage.platform.component.runtime.stateprops.PropertiesControllerInitialState;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesController;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesControllerFactory;
import org.hage.platform.component.runtime.unitmove.UnitConfiguration;
import org.hage.platform.component.structure.distribution.LocalPositionsController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.*;
import static org.hage.platform.component.runtime.activepopulation.PopulationControllerInitialState.initialStateWithControlAgentAndAgents;
import static org.hage.platform.component.runtime.unit.UnitAssembler.unitAssembler;

@Slf4j
@SingletonComponent
class UnitLifecycleProcessor {

    @Autowired(required = false)
    private List<UnitActivationCallback> unitActivationCallbacks = emptyList();
    @Autowired(required = false)
    private List<UnitDeactivationCallback> unitDeactivationCallbacks = emptyList();
    @Autowired
    private LocalPositionsController localPositionsController;

    @Autowired
    private UnitComponentCreationControllerFactory componentCreationControllerFactory;
    @Autowired
    private UnitLocationControllerFactory locationControllerFactory;
    @Autowired
    private UnitActivePopulationControllerFactory activePopulationControllerFactory;
    @Autowired
    private UnitPropertiesControllerFactory propertiesControllerFactory;
    @Autowired
    private AgentContextAdapterFactory agentContextAdapterFactory;
    @Autowired
    private UnitContainerFactory unitContainerFactory;

    public void performPostConstruction(AgentsUnit unit) {

        if (!unit.isInitialized()) {
            log.debug("Initializing agents unit in position {}", unit.getPosition());

            initUnitComponents(unit, empty());
            notifyUnitCreated(unit);
        }

    }

    public void performPostConstruction(AgentsUnit unit, UnitConfiguration unitConfiguration) {
        if (!unit.isInitialized()) {
            log.debug("Initializing agents unit after migration in position {}", unit.getPosition());

            initUnitComponents(unit, of(unitConfiguration));
            notifyUnitCreated(unit);
        } else {
            log.error("Trying to initialize already initialized unit on {} using configuration", unit.getPosition());
        }
    }

    public final void performDestruction(AgentsUnit unit) {
        notifyUnitDestroyed(unit);
    }

    private void initUnitComponents(AgentsUnit unit, Optional<UnitConfiguration> optionalConfiguration) {
        UnitLocationController locationCtrl = locationControllerFactory.createForPosition(unit.getPosition());
        UnitActivePopulationController unitActivePopulationCtrl = createActivePopulationController(unit, optionalConfiguration);
        UnitComponentCreationController unitComponentCreationCtrl = componentCreationControllerFactory.createControllerWithTargetEnv(unitActivePopulationCtrl);
        UnitPropertiesController unitPropertiesController = createUnitPropertiesController(unit, optionalConfiguration);

        unitAssembler()
            .withLocationCtrl(locationCtrl)
            .withUnitActivePopulationCtrl(unitActivePopulationCtrl)
            .withUnitComponentCreationCtrl(unitComponentCreationCtrl)
            .withUnitPropertiesController(unitPropertiesController)
            .withUnitContainer(unitContainerFactory.newUnitContainer())
            // TODO: is generic, move to assembler
            .withAgentContextAdapter(agentContextAdapterFactory.createAgentContextAdapter(locationCtrl, unitComponentCreationCtrl, unitActivePopulationCtrl, unitPropertiesController))

            .assemble(unit);
    }

    private UnitPropertiesController createUnitPropertiesController(AgentsUnit unit, Optional<UnitConfiguration> optionalConfiguration) {
        return optionalConfiguration
            .flatMap(conf -> ofNullable(conf.getPropertiesUpdater()))
            .map(PropertiesControllerInitialState::initialStateWith)
            .map(initialState -> propertiesControllerFactory.createPropertiesControllerWithInitialState(unit.getPosition(), initialState))
            .orElse(propertiesControllerFactory.createUnitPropertiesController(unit.getPosition()));
    }

    private UnitActivePopulationController createActivePopulationController(AgentExecutionContextEnvironment agentsExecEnvironment, Optional<UnitConfiguration> optionalConfiguration) {
        return optionalConfiguration
            .map(conf -> initialStateWithControlAgentAndAgents(conf.getControlAgent(), conf.getAgents()))
            .map(initialState -> activePopulationControllerFactory.createControllerWithExecutionEnvironmentAndInitialState(agentsExecEnvironment, initialState))
            .orElse(activePopulationControllerFactory.createControllerWithExecutionEnvironment(agentsExecEnvironment));
    }

    private void notifyUnitCreated(AgentsUnit unit) {
        unitActivationCallbacks.forEach(activationAware -> activationAware.onUnitActivated(unit));
        localPositionsController.activateLocally(unit.getPosition());
    }

    private void notifyUnitDestroyed(AgentsUnit unit) {
        unitDeactivationCallbacks.forEach(deactivationAware -> deactivationAware.onAgentsUnitDeactivated(unit));
        localPositionsController.deactivateLocally(unit.getPosition());
    }

}
