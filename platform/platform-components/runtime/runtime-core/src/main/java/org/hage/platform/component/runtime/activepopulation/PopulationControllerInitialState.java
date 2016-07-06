package org.hage.platform.component.runtime.activepopulation;

import lombok.Getter;
import org.hage.platform.simulation.runtime.control.ControlAgent;

import java.util.Optional;

import static java.util.Optional.ofNullable;

// TODO: change it to support agents
public class PopulationControllerInitialState {
    @Getter
    private final Optional<ControlAgent> controlAgent;

    private PopulationControllerInitialState(Optional<ControlAgent> controlAgent) {
        this.controlAgent = controlAgent;
    }

    public static PopulationControllerInitialState initialStateWith(ControlAgent controlAgent) {
        return new PopulationControllerInitialState(ofNullable(controlAgent));
    }

}
