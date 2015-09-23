/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 2011-10-20
 * $Id$
 */

package org.jage.solution;


import org.jage.strategy.IStrategy;


/**
 * Factory interface for creating {@link ISolution} instances.
 *
 * @param <S> The type of ISolution created by this factory.
 * @author AGH AgE Team
 */
public interface ISolutionFactory<S extends ISolution> extends IStrategy {

    /**
     * Creates an empty solution.
     *
     * @return an empty solution
     */
    public S createEmptySolution();

    /**
     * Creates an initialized solution.
     *
     * @return an initialized solution
     */
    public S createInitializedSolution();

    /**
     * Returns a new solution, identical to the given one.
     *
     * @param solution The solution to be copied
     * @return A new, copied solution
     */
    public S copySolution(S solution);
}
