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
 * Created: 2012-01-02
 * $Id$
 */

package org.hage.genetic.preselection;


import org.hage.population.IPopulation;
import org.hage.population.IPopulation.Tuple;
import org.hage.solution.ISolution;
import org.hage.strategy.AbstractStrategy;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.sort;


/**
 * Rank preselection, acting as a decorator on some other preselection.
 * <p>
 * Ranks the given population, then update each solution's evaluation to be inversely proportional to that solution's
 * rank, i.e. the worst will have fitness 1 and the best will have fitness N.
 *
 * @author AGH AgE Team
 */
public class RankPreselection<S extends ISolution> extends AbstractStrategy implements IPreselection<S, Double> {

    @Inject
    private IPreselection<S, Double> preselection;

    @Override
    public IPopulation<S, Double> preselect(final IPopulation<S, Double> population) {
        checkNotNull(population, "The population must not be null");

        final List<Tuple<S, Double>> tuples = newArrayList(population.asTupleList());
        sort(tuples, new Comparator<Tuple<S, Double>>() {

            @Override
            public int compare(final Tuple<S, Double> t1, final Tuple<S, Double> t2) {
                return t1.getEvaluation().compareTo(t2.getEvaluation());
            }
        });

        for(int i = 0, n = tuples.size(); i < n; i++) {
            population.setEvaluation(tuples.get(i).getSolution(), Double.valueOf(i));
        }

        return preselection.preselect(population);
    }
}
