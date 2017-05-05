package org.hage.platform.node.runtime.stateprops;

import lombok.Getter;
import org.hage.platform.simulation.runtime.state.UnitPropertiesUpdater;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class PropertiesControllerInitialState {
    @Getter
    private final Optional<UnitPropertiesUpdater> propertiesUpdater;

    private PropertiesControllerInitialState(Optional<UnitPropertiesUpdater> propertiesUpdater) {
        this.propertiesUpdater = propertiesUpdater;
    }

    public static PropertiesControllerInitialState initialStateWith(UnitPropertiesUpdater propertiesUpdater) {
        return new PropertiesControllerInitialState(ofNullable(propertiesUpdater));
    }
}
