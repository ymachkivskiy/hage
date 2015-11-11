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
 * Created: 2011-12-19
 * $Id$
 */

package org.hage.genetic.action;


import org.hage.action.AbstractPerformActionStrategy;
import org.hage.action.IActionContext;
import org.hage.agent.AgentException;
import org.hage.agent.IAgent;
import org.hage.evaluation.IPopulationEvaluator;
import org.hage.population.IPopulation;
import org.hage.solution.ISolution;
import org.hage.utils.JageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.hage.genetic.agent.GeneticActionDrivenAgent.Properties.POPULATION;
import static org.hage.utils.JageUtils.getPropertyValueOrThrowException;


/**
 * This action handler evaluates the population of an agent.
 *
 * @param <S> the type of solutions
 * @param <E> the type of evaluations
 * @author AGH AgE Team
 */
public final class EvaluationActionStrategy<S extends ISolution, E> extends AbstractPerformActionStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(EvaluationActionStrategy.class);

    @Inject
    private IPopulationEvaluator<S, E> populationEvaluator;

    @Override
    public void perform(final IAgent target, final IActionContext context) throws AgentException {
        LOG.debug("Performing evaluation on agent {}.", target.getAddress());

        final IPopulation<S, E> population = getPropertyValueOrThrowException(target, POPULATION);
        populationEvaluator.evaluatePopulation(population);

        if(LOG.isDebugEnabled()) {
            LOG.debug(JageUtils.getPopulationLog(population, "Population Evaluations"));
        }
    }
}