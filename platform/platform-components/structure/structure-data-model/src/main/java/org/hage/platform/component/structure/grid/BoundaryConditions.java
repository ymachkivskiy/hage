package org.hage.platform.component.structure.grid;

import java.io.Serializable;

// TODO [ASK]: do we need extended boundary conditions
public enum BoundaryConditions implements Serializable {
    CLOSED,
//    DEPTH_TORUS,
//    HORIZONTAL_TORUS,
//    VERTICAL_TORUS,
//    DEPTH_AND_HORIZONTAL_TORUS,
//    DEPTH_AND_VERTICAL_TORUS,
//    HORIZONTAL_AND_DEPTH_TORUS,
    FULL_TORUS
}
