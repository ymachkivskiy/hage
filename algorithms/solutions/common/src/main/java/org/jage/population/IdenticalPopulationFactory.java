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

package org.jage.population;


import org.jage.property.ClassPropertyContainer;
import org.jage.solution.ISolution;
import org.jage.solution.ISolutionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.jage.population.Populations.newPopulation;


/**
 * An implementation of {@link IPopulationFactory} which creates a population of identical solutions. <br />
 * <br />
 * It relies on a {@link ISolutionFactory} to create a first solution prototype, then clones it some number of times to
 * form the population.
 *
 * @param <S> The type of solutions created by this factory
 * @author AGH AgE Team
 */
public final class IdenticalPopulationFactory<S extends ISolution> extends ClassPropertyContainer implements
                                                                                                  IPopulationFactory<S> {

    private static final Logger LOG = LoggerFactory.getLogger(IdenticalPopulationFactory.class);

    private static final int DEFAULT_POPULATION_SIZE = 10;

    @Inject
    private ISolutionFactory<S> solutionFactory;

    private int populationSize = DEFAULT_POPULATION_SIZE;

    /**
     * Creates a IdenticalPopulationFactory with a default population size of 10.
     */
    public IdenticalPopulationFactory() {
        this(DEFAULT_POPULATION_SIZE);
    }

    /**
     * Creates a IdenticalPopulationFactory with a given population size.
     *
     * @param populationSize the size of the population to be created by this factory
     */
    private IdenticalPopulationFactory(final int populationSize) {
        this.populationSize = populationSize;
    }

    public void setPopulationSize(final int populationSize) {
        this.populationSize = populationSize;
    }

    @Override
    public <E> IPopulation<S, E> createPopulation() {
        LOG.debug("Creating an identical population of size {}", populationSize);

        final S prototype = solutionFactory.createInitializedSolution();

        final List<S> solutions = new ArrayList<S>(populationSize);
        for(int i = 0; i < populationSize; i++) {
            solutions.add(solutionFactory.copySolution(prototype));
        }

        return newPopulation(solutions);
    }
}
