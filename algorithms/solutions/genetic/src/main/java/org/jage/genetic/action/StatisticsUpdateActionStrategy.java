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
 * Created: 2011-05-10
 * $Id$
 */

package org.jage.genetic.action;


import org.jage.action.AbstractPerformActionStrategy;
import org.jage.action.IActionContext;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.population.IPopulation;
import org.jage.population.IPopulation.Tuple;
import org.jage.solution.ISolution;
import org.jage.solution.ISolutionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Comparator;

import static java.util.Collections.max;
import static org.jage.agent.ActionDrivenAgent.Properties.STEP;
import static org.jage.genetic.agent.GeneticActionDrivenAgent.Properties.BEST_EVER;
import static org.jage.genetic.agent.GeneticActionDrivenAgent.Properties.BEST_EVER_STEP;
import static org.jage.genetic.agent.GeneticActionDrivenAgent.Properties.CURRENT_BEST;
import static org.jage.genetic.agent.GeneticActionDrivenAgent.Properties.POPULATION;
import static org.jage.population.IPopulation.Tuple.newTuple;
import static org.jage.utils.JageUtils.getPropertyValueOrThrowException;
import static org.jage.utils.JageUtils.setPropertyValueOrThrowException;


/**
 * This action handler performs a transformation on an agent's population, using a given variation operator algorithm.
 * The agent's population is replaced with the transformed one.
 *
 * @param <S> the type of solutions
 * @author AGH AgE Team
 */
public final class StatisticsUpdateActionStrategy<S extends ISolution> extends AbstractPerformActionStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(StatisticsUpdateActionStrategy.class);
    private final TupleComparator<S> comparator = new TupleComparator<S>();
    @Inject
    private ISolutionFactory<S> solutionFactory;
    private int resolution = 20;

    public void setResolution(final int resolution) {
        this.resolution = resolution;
    }

    @Override
    public void perform(final IAgent target, final IActionContext context) throws AgentException {
        final Long step = getPropertyValueOrThrowException(target, STEP);

        if(step % resolution == 0) {
            LOG.debug("Updating statistics of agent {}", target.getAddress());

            final IPopulation<S, Double> population = getPropertyValueOrThrowException(target, POPULATION);

            final Tuple<S, Double> currentBest = copyTuple(max(population.asTupleList(), comparator));
            setPropertyValueOrThrowException(target, CURRENT_BEST, currentBest);

            final Tuple<S, Double> bestEver = getPropertyValueOrThrowException(target, BEST_EVER);
            if(comparator.compare(currentBest, bestEver) > 0) {
                setPropertyValueOrThrowException(target, BEST_EVER, currentBest);
                setPropertyValueOrThrowException(target, BEST_EVER_STEP, step);
            }
        }
    }

    private Tuple<S, Double> copyTuple(final Tuple<S, Double> tuple) {
        return newTuple(solutionFactory.copySolution(tuple.getSolution()), tuple.getEvaluation());
    }


    /**
     * Helper Tuple<?, Double> comparator.
     *
     * @author AGH AgE Team
     */
    private static final class TupleComparator<S extends ISolution> implements Comparator<Tuple<S, Double>> {

        @Override
        public int compare(final Tuple<S, Double> t1, final Tuple<S, Double> t2) {
            return t1.getEvaluation().compareTo(t2.getEvaluation());
        }
    }
}
