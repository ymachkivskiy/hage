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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.jage.action.IActionContext;
import org.jage.agent.IAgent;
import org.jage.property.Property;
import org.jage.solution.ISolution;

import static org.jage.agent.ActionDrivenAgent.Properties.STEP;

/**
 * Tests for {@link StatisticsUpdateActionStrategy}.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class StatisticsUpdateActionStrategyTest  {

	private final StatisticsUpdateActionStrategy<ISolution> strategy = new StatisticsUpdateActionStrategy<ISolution>();

	@Test
	public void shouldRespectResolution() throws Exception {
		// given
		final Property stepProperty = mock(Property.class);
		final IAgent target = mock(IAgent.class);
		final IActionContext context = mock(IActionContext.class);

		given(target.getProperty(STEP)).willReturn(stepProperty);
		given(stepProperty.getValue()).willReturn(21L);

		// when
		strategy.perfom(target, context);

		// then
		Mockito.verify(target).getProperty(STEP);
		Mockito.verifyNoMoreInteractions(target);
	}
}
