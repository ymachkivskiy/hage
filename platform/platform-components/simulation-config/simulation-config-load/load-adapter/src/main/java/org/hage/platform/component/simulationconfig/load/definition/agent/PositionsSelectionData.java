package org.hage.platform.component.simulationconfig.load.definition.agent;

import lombok.Getter;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;


public final class PositionsSelectionData {
    private static final PositionsSelectionData ALL = new PositionsSelectionData(SelectionMode.ALL, null);
    private static final PositionsSelectionData RANDOM = new PositionsSelectionData(SelectionMode.RANDOM_CHOOSE__RANDOM_NUMBER, null);

    @Getter
    private final SelectionMode mode;
    @Getter
    private final Optional<Integer> value;

    private PositionsSelectionData(SelectionMode mode, Integer value) {
        checkArgument(value == null || value >= 0, "Negative values not allowed");

        this.mode = mode;
        this.value = ofNullable(value);
    }

    public static PositionsSelectionData allPositions() {
        return ALL;
    }

    public static PositionsSelectionData randomPositions() {
        return RANDOM;
    }

    public static PositionsSelectionData randomPositions(int numberOfPositions) {
        return new PositionsSelectionData(SelectionMode.RANDOM_CHOOSE__FIXED_NUMBER, numberOfPositions);
    }
}
