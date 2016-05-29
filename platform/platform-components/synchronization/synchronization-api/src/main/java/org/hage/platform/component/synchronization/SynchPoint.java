package org.hage.platform.component.synchronization;

import lombok.Data;

@Data
public class SynchPoint {
    private final String name;

    private SynchPoint(String name) {
        this.name = name;
    }

    public static SynchPoint stepPoint(long stepNumber) {
        return new SynchPoint("step-" + stepNumber);
    }

    public static SynchPoint stepPointSubphase(long stepNumber, String subPhase) {
        return new SynchPoint("step-" + stepNumber + "[" + subPhase + "]");
    }

    public static SynchPoint pointForName(String name) {
        return new SynchPoint(name);
    }

}
