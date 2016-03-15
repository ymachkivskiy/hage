package org.hage.platform.component.structure.definition;

import java.io.Serializable;

// TODO: should be defined as calculator of neighbor positions on edge
public enum BoundaryConditions implements Serializable {
    CLOSED,
    SIDE_TORUS,
    TORUS
}
