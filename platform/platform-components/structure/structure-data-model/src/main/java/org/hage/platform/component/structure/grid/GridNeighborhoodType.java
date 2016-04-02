package org.hage.platform.component.structure.grid;

import java.io.Serializable;

public enum GridNeighborhoodType implements Serializable {
    /**
     * Next to central position layer (for one relative position)
     *  ___ ___ ___
     * |   |   |   |
     * |___|___|___|
     * |   |   |   |
     * |___|_ _|___|
     * |   |   |   |
     * |___|___|___|
     *
     */
    NO_NEIGHBORS,
    /**
     * Next to central position layer (for one relative position)
     *  ___ ___ ___
     * |   |   |   |
     * |___|___|___|
     * |   | f |   |
     * |___|_ _|___|
     * |   |   |   |
     * |___|___|___|
     *
     */
    FIRST_DEGREE,
    /**
     * Next to central position layer (for one relative position)
     *  ___ ___ ___
     * |   | s |   |
     * |___|___|___|
     * | s | f | s |
     * |___|_ _|___|
     * |   | s |   |
     * |___|___|___|
     *
     */
    SECOND_DEGREE,
    /**
     * Next to central position layer (for one relative position)
     *  ___ ___ ___
     * | t | s | t |
     * |___|___|___|
     * | s | f | s |
     * |___|_ _|___|
     * | t | s | t |
     * |___|___|___|
     *
     */
    THIRD_DEGREE,
}
