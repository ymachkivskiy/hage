package org.hage.platform.node.structure.distribution;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.annotation.di.SingletonComponent;
import org.hage.platform.node.structure.Position;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@SingletonComponent
@Slf4j
public class StructureChangeRemoteBuffer  {

    @Autowired
    private StructureChangedDistributor distributionEndpoint;

    private final List<Position> activated = new LinkedList<>();
    private final List<Position> deactivated = new LinkedList<>();

    public synchronized void onStepPerformed() {
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

}
