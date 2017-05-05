package org.hage.platform.node.runtime.unit;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.runtime.unitmove.UnitConfiguration;
import org.hage.platform.node.structure.distribution.LocalPositionsController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

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
    private UnitAssembler unitAssembler;

    public void performConstruction(AgentsUnit unit, Optional<UnitConfiguration> optionalConfiguration) {

        if (!unit.isInitialized()) {
            log.debug("Initializing agents unit in position {}", unit.getPosition());

            unitAssembler.assembleUnit(unit, optionalConfiguration);
            notifyUnitCreated(unit);
        }

    }

    public final void performDestruction(AgentsUnit unit) {
        notifyUnitDestroyed(unit);
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
