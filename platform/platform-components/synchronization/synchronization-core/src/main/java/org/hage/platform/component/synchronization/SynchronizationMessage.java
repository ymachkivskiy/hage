package org.hage.platform.component.synchronization;

import lombok.Data;

import java.io.Serializable;

@Data
public class SynchronizationMessage implements Serializable {
    private final String synchronizationPoint;
}
