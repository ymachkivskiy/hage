package org.hage.platform.component.structure.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.HageComponent;
import org.hage.platform.component.runtime.execution.BaseStaticExecutionStateAware;
import org.hage.platform.component.structure.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.hage.platform.component.runtime.execution.PostStepPhase.STRUCTURE_UPDATE;

@HageComponent
@Slf4j
class StructureChangeRemoteBuffer extends BaseStaticExecutionStateAware {

    @Autowired
    private StructureDistributionEndpoint distributionEndpoint;

    private final List<Position> activated = new LinkedList<>();
    private final List<Position> deactivated = new LinkedList<>();

    public StructureChangeRemoteBuffer() {
        super(STRUCTURE_UPDATE);
    }

    @Override
    public synchronized void onStepPerformed(long stepNumber) {
        if (isChangeStored()) {
            notifyRemotes();
            resetBuffer();
        }
    }

    synchronized void addActivated(Collection<Position> positions) {
        log.debug("Add activated positions {}", positions);

        activated.addAll(positions);
    }

    synchronized void addDeactivated(List<Position> positions) {
        log.debug("Add deactivated positions {}", positions);

        deactivated.addAll(positions);
    }

    private boolean isChangeStored() {
        return !activated.isEmpty() || !deactivated.isEmpty();
    }

    private void notifyRemotes() {
        distributionEndpoint.updatePositions(
            new ArrayList<>(activated),
            new ArrayList<>(deactivated)
        );
    }

    private void resetBuffer() {
        activated.clear();
        deactivated.clear();
    }

    @Override
    public String toString() {
        return getPhase().toString() + "-phased " + this.getClass().getSimpleName();
    }
}
