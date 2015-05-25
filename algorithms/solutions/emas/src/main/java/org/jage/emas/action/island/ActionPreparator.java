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

package org.jage.emas.action.island;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.jage.action.Action;
import org.jage.action.SingleAction;
import org.jage.action.preparators.IActionPreparator;
import org.jage.address.agent.AgentAddress;
import org.jage.address.selector.AddressSelector;
import org.jage.address.selector.Selectors;
import org.jage.emas.agent.IslandAgent;
import org.jage.emas.util.ChainingContext;
import org.jage.emas.util.ChainingContext.ChainingContextBuilder;
import org.jage.strategy.AbstractStrategy;

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
public class ActionPreparator extends AbstractStrategy implements IActionPreparator<IslandAgent> {

	private final ChainingContext initializationContext;

	private final List<ChainingContext> otherContexts;

	/**
	 * Create an {@link ActionPreparator} for the given initialization context and list of other contexts.
	 *
	 * @param initializationContext
	 *            the initialization context
	 * @param otherContexts
	 *            a list of other contexts, may be empty
	 */
	@Inject
	public ActionPreparator(final ChainingContext initializationContext, final List<ChainingContext> otherContexts) {
		this.initializationContext = checkNotNull(initializationContext);
		this.otherContexts = checkNotNull(otherContexts);
	}

	@Override
	public List<Action> prepareActions(final IslandAgent agent) {
		final ChainingContextBuilder builder = ChainingContext.builder()
		        .appendIf(agent.getStep() == 0, initializationContext).appendAll(otherContexts);
		if (builder.isEmpty()) {
			return Collections.emptyList();
		}

		final AddressSelector<AgentAddress> target = Selectors.singleAddress(agent.getAddress());
		return Collections.<Action> singletonList(new SingleAction(target, builder.build()));
	}
}
