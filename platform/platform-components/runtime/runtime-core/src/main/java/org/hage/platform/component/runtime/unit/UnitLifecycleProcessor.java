package org.hage.platform.component.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.component.runtime.activepopulation.AgentsController;
import org.hage.platform.component.runtime.activepopulation.AgentsTargetEnvironment;
import org.hage.platform.component.runtime.activepopulation.UnitActivePopulationController;
import org.hage.platform.component.runtime.container.AgentsCreator;
import org.hage.platform.component.runtime.container.UnitAgentCreationController;
import org.hage.platform.component.runtime.location.UnitLocationController;
import org.hage.platform.component.runtime.unit.context.AgentContextAdapter;
import org.hage.platform.component.runtime.unit.context.AgentExecutionContextEnvironment;
import org.hage.platform.component.structure.Position;
import org.hage.platform.component.structure.distribution.LocalPositionsController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
public abstract class UnitLifecycleProcessor {

    @Autowired(required = false)
    private List<UnitActivationAware> unitActivationAwares = emptyList();
    @Autowired(required = false)
    private List<UnitDeactivationAware> unitDeactivationAwares = emptyList();
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
        //todo : NOT IMPLEMENTED

    }

    private void initUnitComponents(AgentsUnit unit) {
        UnitActivePopulationController unitActivePopulationCtrl = createUnitPopulationController(unit);
        UnitLocationController locationCtrl = createUnitLocationContext(unit.getPosition());
        UnitAgentCreationController unitAgentCreationCtrl = createUnitPopulationContext(unitActivePopulationCtrl);

        unit.setUnitLocationController(locationCtrl);
        unit.setUnitAgentCreationController(unitAgentCreationCtrl);
        unit.setUnitActivePopulationController(unitActivePopulationCtrl);
        unit.setAgentContextAdapter(createAgentContextAdapter(locationCtrl, unitAgentCreationCtrl, unitActivePopulationCtrl));
    }

    private void notifyUnitCreated(AgentsUnit unit) {
        unitActivationAwares.forEach(activationAware -> activationAware.onUnitActivated(unit));
        localPositionsController.activateLocally(unit.getPosition());
    }

    protected abstract AgentContextAdapter createAgentContextAdapter(UnitLocationController locationController, AgentsCreator agentsCreator, AgentsController agentsController);

    protected abstract UnitAgentCreationController createUnitPopulationContext(AgentsTargetEnvironment unitActivePopulationController);

    protected abstract UnitLocationController createUnitLocationContext(Position position);

    protected abstract UnitActivePopulationController createUnitPopulationController(AgentExecutionContextEnvironment agentEnvironment);


}
