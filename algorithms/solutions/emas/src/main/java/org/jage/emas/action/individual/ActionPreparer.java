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

package org.jage.emas.action.individual;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.jage.action.Action;
import org.jage.action.SingleAction;
import org.jage.action.preparers.IActionPreparer;
import org.jage.address.agent.AgentAddress;
import org.jage.address.selector.AddressSelector;
import org.jage.address.selector.Selectors;
import org.jage.emas.agent.IndividualAgent;
import org.jage.emas.util.ChainingContext;
import org.jage.emas.util.ChainingContext.ChainingContextBuilder;
import org.jage.strategy.AbstractStrategy;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Action preparator for individual agents. It creates a chaining action context from the list provided in its
 * constructor, and applies it on each target agent.
 * <p>
 * If an agents step is 0, it is skipped (in order to allow aggregates to initialize it before). This might be changed
 * when some preprocess phase for initialization is added.
 *
 * @author AGH AgE Team
 */
public class ActionPreparer extends AbstractStrategy implements IActionPreparer<IndividualAgent> {

	private final List<ChainingContext> contexts;

	/**
	 * Create an {@link ActionPreparer} for the given list of contexts.
	 *
	 * @param contexts
	 *            the contexts for this preparator, may be empty
	 */
	@Inject
	public ActionPreparer(final List<ChainingContext> contexts) {
		this.contexts = checkNotNull(contexts);
	}

	@Override
	public List<Action> prepareActions(final IndividualAgent agent) {
		final boolean condition = agent.getEnvironment().getStep() != 0;
		final ChainingContextBuilder builder = ChainingContext.builder().appendAllIf(condition, contexts);
		if (builder.isEmpty()) {
			return Collections.emptyList();
		}

		final AddressSelector<AgentAddress> target = Selectors.singleAddress(agent.getAddress());
		return Collections.<Action> singletonList(new SingleAction(target, builder.build()));
	}
}
