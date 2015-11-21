package org.hage.platform.config.def.agent;

import lombok.Data;

@Data
public final class InternalPositionsSelectionData {
    private final SelectionMode mode;
    private final Integer value;
}
