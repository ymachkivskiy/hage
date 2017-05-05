package org.hage.platform.cluster.synch;

import lombok.Data;

import java.io.Serializable;

@Data
public class SynchronizationMessage implements Serializable {
    private final String synchronizationPoint;
}
