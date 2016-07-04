package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationControllerFactory;
import org.hage.platform.component.runtime.container.UnitComponentCreationController;
import org.hage.platform.component.runtime.container.UnitComponentCreationControllerFactory;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.location.UnitLocationControllerFactory;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesController;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesControllerFactory;
import org.hage.platform.component.structure.distribution.LocalPositionsController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.emptyList;

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

    public final void performPostConstruction(AgentsUnit unit) {

        if (!unit.isInitialized()) {
            log.debug("Initializing agents unit in position {}", unit);

            initUnitComponents(unit);
            notifyUnitCreated(unit);
        }

    }

    public final void performDestruction(AgentsUnit unit) {
        notifyUnitDestroyed(unit);
    }

    private void initUnitComponents(AgentsUnit unit) {
        UnitLocationController locationCtrl = locationControllerFactory.createForPosition(unit.getPosition());
        UnitActivePopulationController unitActivePopulationCtrl = activePopulationControllerFactory.createActivePopulationControllerForExecutionEnvironment(unit);
        UnitComponentCreationController unitComponentCreationCtrl = componentCreationControllerFactory.createControllerWithTargetEnv(unitActivePopulationCtrl);
        UnitPropertiesController unitPropertiesController = propertiesControllerFactory.createUnitPropertiesController(unit.getPosition(), unitComponentCreationCtrl);
        AgentContextAdapter agentContextAdapter = agentContextAdapterFactory.createAgentContextAdapter(locationCtrl, unitComponentCreationCtrl, unitActivePopulationCtrl, unitPropertiesController);

        unitActivePopulationCtrl.setLocalDependenciesInjector(unitComponentCreationCtrl);

        unit.setUnitPropertiesController(unitPropertiesController);
        unit.setUnitLocationController(locationCtrl);
        unit.setUnitComponentCreationController(unitComponentCreationCtrl);
        unit.setUnitActivePopulationController(unitActivePopulationCtrl);
        unit.setAgentContextAdapter(agentContextAdapter);
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
