package org.hage.platform.node.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.node.runtime.activepopulation.UnitActivePopulationControllerFactory;
import org.hage.platform.node.runtime.container.UnitComponentCreationController;
import org.hage.platform.node.runtime.container.UnitComponentCreationControllerFactory;
import org.hage.platform.node.runtime.location.UnitLocationController;
import org.hage.platform.node.runtime.location.UnitLocationControllerFactory;
import org.hage.platform.node.runtime.stateprops.PropertiesControllerInitialState;
import org.hage.platform.node.runtime.stateprops.UnitPropertiesController;
import org.hage.platform.node.runtime.stateprops.UnitPropertiesControllerFactory;
import org.hage.platform.node.runtime.unitmove.UnitConfiguration;
import org.hage.platform.node.runtime.activepopulation.PopulationControllerInitialState;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@SingletonComponent
@Slf4j
class UnitAssembler {

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

    public void assembleUnit(AgentsUnit unit, Optional<UnitConfiguration> optionalConfiguration) {
        log.debug("Assembling unit {} using optional configuration {}", unit, optionalConfiguration);

        UnitLocationController locationCtrl = locationControllerFactory.createForPosition(unit.getPosition());
        UnitActivePopulationController unitActivePopulationCtrl = createActivePopulationController(unit, optionalConfiguration);
        UnitComponentCreationController unitComponentCreationCtrl = componentCreationControllerFactory.createControllerWithTargetEnv(unitActivePopulationCtrl);
        UnitPropertiesController unitPropertiesController = createUnitPropertiesController(unit, optionalConfiguration);
        UnitContainer componentsCommon = unitContainerFactory.newUnitContainer();

        AgentContextAdapter agentContextAdapter = agentContextAdapterFactory.createAgentContextAdapter(locationCtrl, unitComponentCreationCtrl, unitActivePopulationCtrl, unitPropertiesController);

        unit.setUnitPropertiesController(unitPropertiesController);
        unit.setUnitLocationController(locationCtrl);
        unit.setUnitComponentCreationController(unitComponentCreationCtrl);
        unit.setUnitActivePopulationController(unitActivePopulationCtrl);
        unit.setAgentContextAdapter(agentContextAdapter);

        locationCtrl.setUnitContainer(componentsCommon);
        unitActivePopulationCtrl.setUnitContainer(componentsCommon);
        unitComponentCreationCtrl.setUnitContainer(componentsCommon);
        unitPropertiesController.setUnitContainer(componentsCommon);

        locationCtrl.performPostConstruction();
        unitActivePopulationCtrl.performPostConstruction();
        unitComponentCreationCtrl.performPostConstruction();
        unitPropertiesController.performPostConstruction();
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
            .map(conf -> PopulationControllerInitialState.initialStateWithControlAgentAndAgents(conf.getControlAgent(), conf.getAgents()))
            .map(initialState -> activePopulationControllerFactory.createControllerWithExecutionEnvironmentAndInitialState(agentsExecEnvironment, initialState))
            .orElse(activePopulationControllerFactory.createControllerWithExecutionEnvironment(agentsExecEnvironment));
    }
}
