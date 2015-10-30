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
 * Created: 2008-10-12
 * $Id$
 */

package org.jage.genetic.preselection;


import org.jage.population.IPopulation;
import org.jage.solution.ISolution;
import org.jage.strategy.IStrategy;


/**
 * Preselection strategy interface.
 *
 * @param <S> The type of {@link ISolution} to be preselected.
 * @param <E> The type of evaluations
 * @author AGH AgE Team
 */
public interface IPreselection<S extends ISolution, E> extends IStrategy {

    /**
     * Preselects a given population.
     *
     * @param population The population to be preselected
     * @return Preselected solutions
     */
    public IPopulation<S, E> preselect(IPopulation<S, E> population);

}
