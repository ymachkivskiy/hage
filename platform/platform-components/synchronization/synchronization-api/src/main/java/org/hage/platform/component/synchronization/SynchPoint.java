package org.hage.platform.component.synchronization;

import lombok.Data;

@Data
public class SynchPoint {
    private final long stepNumber;
    private final String subPhase;
}
