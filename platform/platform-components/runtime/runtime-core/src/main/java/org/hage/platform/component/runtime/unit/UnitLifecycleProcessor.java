package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.activepopulation.AgentsController;
import org.hage.platform.component.runtime.activepopulation.AgentsTargetEnvironment;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.component.runtime.container.AgentsCreator;
import org.hage.platform.component.runtime.container.UnitComponentCreationController;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesController;
import org.hage.platform.component.runtime.stateprops.UnitPropertiesProvider;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.distribution.LocalPositionsController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
public abstract class UnitLifecycleProcessor {

    @Autowired(required = false)
    private List<UnitActivationCallback> unitActivationCallbacks = emptyList();
    @Autowired(required = false)
    private List<UnitDeactivationCallback> unitDeactivationCallbacks = emptyList();
    @Autowired
    private LocalPositionsController localPositionsController;

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
        UnitLocationController locationCtrl = createUnitLocationContext(unit.getPosition());
        UnitActivePopulationController unitActivePopulationCtrl = createUnitPopulationController(unit);
        UnitComponentCreationController unitComponentCreationCtrl = createUnitComponentCreationController(unitActivePopulationCtrl);
        UnitPropertiesController unitPropertiesController = createUnitPropertiesController(unit.getPosition(), unitComponentCreationCtrl);

        unitActivePopulationCtrl.setLocalDependenciesInjector(unitComponentCreationCtrl);

        unit.setUnitPropertiesController(unitPropertiesController);
        unit.setUnitLocationController(locationCtrl);
        unit.setUnitComponentCreationController(unitComponentCreationCtrl);
        unit.setUnitActivePopulationController(unitActivePopulationCtrl);
        unit.setAgentContextAdapter(createAgentContextAdapter(locationCtrl, unitComponentCreationCtrl, unitActivePopulationCtrl, unitPropertiesController));
    }

    private void notifyUnitCreated(AgentsUnit unit) {
        unitActivationCallbacks.forEach(activationAware -> activationAware.onUnitActivated(unit));
        localPositionsController.activateLocally(unit.getPosition());
    }

    private void notifyUnitDestroyed(AgentsUnit unit) {
        unitDeactivationCallbacks.forEach(deactivationAware -> deactivationAware.onAgentsUnitDeactivated(unit));
        localPositionsController.deactivateLocally(unit.getPosition());
    }

    protected abstract AgentContextAdapter createAgentContextAdapter(UnitLocationController locationController, AgentsCreator agentsCreator, AgentsController agentsController, UnitPropertiesProvider localUnitPropertiesProvider);

    protected abstract UnitComponentCreationController createUnitComponentCreationController(AgentsTargetEnvironment unitActivePopulationCtrl);

    protected abstract UnitLocationController createUnitLocationContext(Position position);

    protected abstract UnitActivePopulationController createUnitPopulationController(AgentExecutionContextEnvironment agentEnvironment);

    protected abstract UnitPropertiesController createUnitPropertiesController(Position position, UnitComponentCreationController componentCreationController);


}
