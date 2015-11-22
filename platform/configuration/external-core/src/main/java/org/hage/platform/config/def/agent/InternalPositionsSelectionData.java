package org.hage.platform.config.def.agent;

import lombok.Getter;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;


public final class InternalPositionsSelectionData {
    private static final InternalPositionsSelectionData ALL = new InternalPositionsSelectionData(SelectionMode.ALL, null);
    private static final InternalPositionsSelectionData RANDOM = new InternalPositionsSelectionData(SelectionMode.RANDOM_CHOOSE__RANDOM_NUMBER, null);

    @Getter
    private final SelectionMode mode;
    @Getter
    private final Optional<Long> value;

    private InternalPositionsSelectionData(SelectionMode mode, Long value) {
        checkArgument(value == null || value > 0);

        this.mode = mode;
        this.value = ofNullable(value);
    }

    public static InternalPositionsSelectionData allPositions() {
        return ALL;
    }

    public static InternalPositionsSelectionData randomPositions() {
        return RANDOM;
    }

    public static InternalPositionsSelectionData randomPositions(long numberOfPositions) {
        return new InternalPositionsSelectionData(SelectionMode.RANDOM_CHOOSE__FIXED_NUMBER, numberOfPositions);
    }
}
