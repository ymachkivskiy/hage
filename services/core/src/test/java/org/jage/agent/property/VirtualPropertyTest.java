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
 * Created: 2009-05-20
 * $Id$
 */

package org.jage.agent.property;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.jage.address.agent.AgentAddress;
import org.jage.agent.AggregateActionService;
import org.jage.agent.AggregateQueryService;
import org.jage.agent.SimpleAgent;
import org.jage.agent.SimpleAggregate;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.Property;
import org.jage.workplace.SimpleWorkplace;

import static org.jage.utils.AgentTestUtils.createSimpleAgentWithoutStep;

/**
 * Tests for virtual properties.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class VirtualPropertyTest {

	private static final String VIRTUAL_PROPERTY_NAME = "address";

	private SimpleAgent agent;

	@Mock
	private IComponentInstanceProvider instanceProvider;

	@Before
	public void setUp() throws Exception {
		final SimpleWorkplace workplace = new SimpleWorkplace();

		final SimpleAggregate aggregate = new SimpleAggregate(mock(AgentAddress.class));
		aggregate.setActionService(mock(AggregateActionService.class));
		aggregate.setQueryService(mock(AggregateQueryService.class));
		aggregate.setInstanceProvider(instanceProvider);
		aggregate.init();
		aggregate.setAgentEnvironment(workplace);

		agent = createSimpleAgentWithoutStep();
		agent.init();
		agent.setAgentEnvironment(aggregate);
	}

	@Test
	public void testGettingVirtualProperty() throws Exception {
		final Property property = agent.getProperty(VIRTUAL_PROPERTY_NAME);
		assertNotNull(property);
		assertEquals(VIRTUAL_PROPERTY_NAME, property.getMetaProperty().getName());
		assertEquals(agent.getAddress(), property.getValue());
	}

	@Test(expected = InvalidPropertyPathException.class)
	public void testInvalidProperty() {
		agent.getProperty("invalidName");
	}

}
