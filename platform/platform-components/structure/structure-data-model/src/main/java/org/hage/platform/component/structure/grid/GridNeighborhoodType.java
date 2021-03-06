package org.hage.platform.component.structure.grid;

import java.io.Serializable;

public enum GridNeighborhoodType implements Serializable {
    /**
     * <b>C</b> for central position
     *
     *  ___ ___ ___  ___ ___ ___  ___ ___ ___
     * |   |   |   ||   |   |   ||   |   |   |
     * |___|___|___||___|___|___||___|___|___|
     * |   |   |   ||   | C |   ||   |   |   |
     * |___|___|___||___|___|___||___|___|___|
     * |   |   |   ||   |   |   ||   |   |   |
     * |___|___|___||___|___|___||___|___|___|
     *
     */
    NO_NEIGHBORS,
    /**
     * <b>C</b> for central position
     *
     *  ___ ___ ___  ___ ___ ___  ___ ___ ___
     * |   |   |   ||   | X |   ||   |   |   |
     * |___|___|___||___|___|___||___|___|___|
     * |   | X |   || X | C | X ||   | X |   |
     * |___|___|___||___|___|___||___|___|___|
     * |   |   |   ||   | X |   ||   |   |   |
     * |___|___|___||___|___|___||___|___|___|
     *
     */
    VON_NEUMANN_NEGIHBORHOOD,
    /**
     * <b>C</b> for central position
     *
     *  ___ ___ ___  ___ ___ ___  ___ ___ ___
     * | X | X | X || X | X | X || X | X | X |
     * |___|___|___||___|___|___||___|___|___|
     * | X | X | X || X | C | X || X | X | X |
     * |___|___|___||___|___|___||___|___|___|
     * | X | X | X || X | X | X || X | X | X |
     * |___|___|___||___|___|___||___|___|___|
     *
     */
    MOORE_NEIGHBORHOOD,
}
