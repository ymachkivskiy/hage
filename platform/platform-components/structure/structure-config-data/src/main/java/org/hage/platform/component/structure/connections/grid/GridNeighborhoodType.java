package org.hage.platform.component.structure.connections.grid;

import java.io.Serializable;

//todo [ASK]: how to express it
public enum GridNeighborhoodType implements Serializable {
    NO_NEIGHBORS,
    SIX_NEIGHBORS_ONLY_MUTUAL,
    EIGHTEEN_NEIGHBORS_CUBE_WITHOUT_CORNERS,
    TWENTY_SIX_NEIGHBORS_FULL_CUBE,
}
