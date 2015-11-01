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
 * Created: 2011-05-05
 * $Id$
 */

package org.hage.genetic.preselection;


import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.hage.population.IPopulation;
import org.hage.solution.ISolutionFactory;
import org.hage.solution.IVectorSolution;
import org.hage.strategy.AbstractStrategy;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static com.google.common.primitives.Doubles.toArray;
import static org.hage.population.Populations.newPopulation;


/**
 * Abtract {@link IPreselection} implementation. Relies on subclasses to provide the indices of the preselected solution
 * (note that any solution may be chosen multiple times).
 *
 * @author AGH AgE Team
 */
public abstract class AbstractPreselection extends AbstractStrategy implements
                                                                    IPreselection<IVectorSolution<Double>, Double> {

    @Inject
    private ISolutionFactory<IVectorSolution<Double>> solutionFactory;

    @Override
    public final IPopulation<IVectorSolution<Double>, Double> preselect(
            final IPopulation<IVectorSolution<Double>, Double> population) {
        checkNotNull(population, "The population must not be null");

        final int populationSize = population.size();
        if(populationSize < 2) {
            return population;
        }

        final List<IVectorSolution<Double>> solutionList = population.asSolutionList();
        final List<Double> evaluationList = population.asEvaluationList();

        final IntSet preselectedIndices = new IntOpenHashSet();
        final List<IVectorSolution<Double>> preselectedSolutions = newArrayListWithCapacity(populationSize);
        for(final int index : getPreselectedIndices(toArray(evaluationList))) {
            IVectorSolution<Double> solution = solutionList.get(index);
            if(!preselectedIndices.add(index)) {
                solution = solutionFactory.copySolution(solution);
            }
            preselectedSolutions.add(solution);
        }
        preselectedIndices.clear();

        return newPopulation(preselectedSolutions);
    }

    /**
     * Performs the actual preselection. Given an array of evaluation values, returns an array containing indices of the
     * preselected solutions. Any given solution may be preselected multiple times, in which case it will be copied in
     * the resulting preselected population.
     *
     * @param values The solutions evaluation values
     * @return The indices of the preselected solutions
     */
    protected abstract int[] getPreselectedIndices(double[] values);
}
