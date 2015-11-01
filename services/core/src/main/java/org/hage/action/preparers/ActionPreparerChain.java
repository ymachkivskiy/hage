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
 * Created: 2011-04-30
 * $Id$
 */

package org.hage.action.preparers;


import org.hage.action.Action;
import org.hage.action.ComplexAction;
import org.hage.agent.IAgent;
import org.hage.strategy.AbstractStrategy;

import javax.inject.Inject;
import java.util.List;

import static java.util.Collections.singletonList;


/**
 * A {@link IActionPreparer} that expects a list of other {@link IActionPreparer}s and build an aggregated complex
 * action upon the actions they prepare. This class allows to chain IActionPreparators.
 *
 * @param <T> a type of the agent that the preparator operates on.
 * @author AGH AgE Team
 */
public class ActionPreparerChain<T extends IAgent> extends AbstractStrategy implements IActionPreparer<T> {

    @Inject
    private List<IActionPreparer<IAgent>> actionPreparers;

    @Override
    public List<Action> prepareActions(final T agent) {

        final ComplexAction complexAction = new ComplexAction();
        for(final IActionPreparer<IAgent> actionPreparer : actionPreparers) {
            actionPreparer.prepareActions(agent).forEach(complexAction::addChild);
        }

        return singletonList(complexAction);
    }

    /**
     * Sets the action preparers that are to be chained by this preparator.
     *
     * @param actionPreparers action preparers to be chained.
     */
    public final void setActionPreparers(final List<IActionPreparer<IAgent>> actionPreparers) {
        this.actionPreparers = actionPreparers;
    }
}
