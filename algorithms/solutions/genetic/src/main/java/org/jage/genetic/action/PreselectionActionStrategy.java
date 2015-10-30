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
 * Created: 2011-04-29
 * $Id$
 */

package org.jage.genetic.action;


import org.jage.action.AbstractPerformActionStrategy;
import org.jage.action.IActionContext;
import org.jage.agent.AgentException;
import org.jage.agent.IAgent;
import org.jage.genetic.preselection.IPreselection;
import org.jage.population.IPopulation;
import org.jage.solution.ISolution;
import org.jage.utils.JageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.jage.genetic.agent.GeneticActionDrivenAgent.Properties.POPULATION;
import static org.jage.utils.JageUtils.getPropertyValueOrThrowException;
import static org.jage.utils.JageUtils.setPropertyValueOrThrowException;


/**
 * This action handler performs a preselection on an agent's population, using a given preselection algorithm. The
 * agent's population is replaced with the preselected one.
 *
 * @param <S> the type of solutions
 * @author AGH AgE Team
 */
public final class PreselectionActionStrategy<S extends ISolution> extends AbstractPerformActionStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(PreselectionActionStrategy.class);

    @Inject
    private IPreselection<S, Double> preselection;

    @Override
    public void perform(final IAgent target, final IActionContext context) throws AgentException {
        LOG.debug("Performing preselection on agent {} population.", target.getAddress());

        final IPopulation<S, Double> originalPopulation = getPropertyValueOrThrowException(target, POPULATION);
        final IPopulation<S, Double> preselectedPopulation = preselection.preselect(originalPopulation);

        setPropertyValueOrThrowException(target, POPULATION, preselectedPopulation);

        if(LOG.isDebugEnabled()) {
            LOG.debug(JageUtils.getPopulationLog(preselectedPopulation, "Preselected solutions"));
        }
    }
}
