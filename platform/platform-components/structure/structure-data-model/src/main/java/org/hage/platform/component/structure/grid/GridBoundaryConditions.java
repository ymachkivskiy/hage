package org.hage.platform.component.structure.grid;

import java.io.Serializable;

public enum GridBoundaryConditions implements Serializable {
    CLOSED,
    DEPTH__TORUS,
    HORIZONTAL__TORUS,
    VERTICAL__TORUS,
    DEPTH_AND_HORIZONTAL__TORUS,
    DEPTH_AND_VERTICAL__TORUS,
    HORIZONTAL_AND_VERTICAL__TORUS,
    FULL__TORUS
}
