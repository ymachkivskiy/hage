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
 * Created: 2012-01-30
 * $Id$
 */

package org.hage.emas.action.island;


import org.hage.action.Action;
import org.hage.action.SingleAction;
import org.hage.action.preparers.IActionPreparer;
import org.hage.emas.agent.IslandAgent;
import org.hage.emas.util.ChainingContext;
import org.hage.emas.util.ChainingContext.ChainingContextBuilder;
import org.hage.platform.util.communication.address.agent.AgentAddress;
import org.hage.platform.util.communication.address.selector.AddressSelector;
import org.hage.platform.util.communication.address.selector.Selectors;
import org.hage.strategy.AbstractStrategy;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Action preparator for island agents. It creates a chaining action context from the list provided in its constructor,
 * and applies it on each target agent.
 * <p>
 * If an agents step is 0, an initialization action is prepended to the chain. This might be changed when some
 * preprocess phase for initialization is added.
 *
 * @author AGH AgE Team
 */
public class ActionPreparer extends AbstractStrategy implements IActionPreparer<IslandAgent> {

    private final ChainingContext initializationContext;

    private final List<ChainingContext> otherContexts;

    /**
     * Create an {@link ActionPreparer} for the given initialization context and list of other contexts.
     *
     * @param initializationContext the initialization context
     * @param otherContexts         a list of other contexts, may be empty
     */
    @Inject
    public ActionPreparer(final ChainingContext initializationContext, final List<ChainingContext> otherContexts) {
        this.initializationContext = checkNotNull(initializationContext);
        this.otherContexts = checkNotNull(otherContexts);
    }

    @Override
    public List<Action> prepareActions(final IslandAgent agent) {
        final ChainingContextBuilder builder = ChainingContext.builder()
                .appendIf(agent.getStep() == 0, initializationContext).appendAll(otherContexts);
        if(builder.isEmpty()) {
            return Collections.emptyList();
        }

        final AddressSelector<AgentAddress> target = Selectors.singleAddress(agent.getAddress());
        return Collections.<Action> singletonList(new SingleAction(target, builder.build()));
    }
}
