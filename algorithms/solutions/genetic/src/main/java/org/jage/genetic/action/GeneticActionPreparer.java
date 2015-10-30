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


import org.jage.action.Action;
import org.jage.action.SingleAction;
import org.jage.action.preparers.IActionPreparer;
import org.jage.address.agent.AgentAddress;
import org.jage.address.selector.AddressSelector;
import org.jage.address.selector.Selectors;
import org.jage.agent.IAgent;
import org.jage.strategy.AbstractStrategy;

import java.util.List;

import static com.google.common.collect.Lists.newArrayListWithCapacity;


/**
 * This preparator sets up agent behavior for a genetic algorithm.
 * <p>
 * <p>
 * The steps are:
 * <ul>
 * <li>preselect the population</li>
 * <li>apply a variation operator on the population (i.e. recombination, mutation)</li>
 * <li>update agent statistics</li>
 * </ul>
 * Additionally, at the first run, some initialization is performed.
 *
 * @param <T> a type of the agent that the preparator operates on.
 * @author AGH AgE Team
 */
public final class GeneticActionPreparer<T extends IAgent> extends AbstractStrategy implements IActionPreparer<T> {

    private InitializationActionContext initializationActionContext = new InitializationActionContext();

    private PreselectionActionContext preselectionActionContext = new PreselectionActionContext();

    private VariationActionContext variationActionContext = new VariationActionContext();

    private EvaluationActionContext evaluationActionContext = new EvaluationActionContext();

    private StatisticsUpdateActionContext statisticsUpdateActionContext = new StatisticsUpdateActionContext();

    private boolean notYetInitialized = true;

    @Override
    public List<Action> prepareActions(IAgent agent) {
        AgentAddress agentAddress = agent.getAddress();
        AddressSelector<AgentAddress> target = Selectors.singleAddress(agentAddress);

        List<Action> actions = newArrayListWithCapacity(5);

        if(notYetInitialized) {
            actions.add(new SingleAction(target, initializationActionContext));
            notYetInitialized = false;
        }

        actions.add(new SingleAction(target, preselectionActionContext));
        actions.add(new SingleAction(target, variationActionContext));
        actions.add(new SingleAction(target, evaluationActionContext));
        actions.add(new SingleAction(target, statisticsUpdateActionContext));

        return actions;
    }
}
