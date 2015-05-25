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
 * Created: 2008-10-07
 * $Id$
 */

package org.jage.agent;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.jage.address.Address;
import org.jage.address.agent.AgentAddress;

import static org.jage.utils.AgentTestUtils.createMockAgentWithAddress;

import com.google.common.collect.ImmutableList;

import static com.google.common.base.Predicates.in;
import static com.google.common.collect.Iterators.all;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Tests the {@link SimpleAggregate} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleAggregateTest {

	@Mock
	private AggregateActionService actionService;

	private final SimpleAggregate aggregate = new SimpleAggregate(mock(AgentAddress.class));

	@Mock
	private ISimpleAgentEnvironment agentEnvironment;

	private final List<Address> path = newArrayList();

	@Before
	public void setUp() {
		aggregate.setActionService(actionService);
		aggregate.setAgentEnvironment(agentEnvironment);
		path.add(aggregate.getAddress());
	}

	@Test
	public void testAdd() {
		// given
		final ISimpleAgent agent = createMockAgentWithAddress();

		// when
		final boolean added = aggregate.add(agent);

		// then
		assertThat(added, is(true));
		assertThat(aggregate.size(), is(1));
	}

	@Test
	public void testAddExisting() {
		// given
		final ISimpleAgent agent = createMockAgentWithAddress();
		aggregate.add(agent);

		// when
		final boolean added = aggregate.add(createMockAgentWithAddress(agent.getAddress()));

		// then
		assertThat(added, is(false));
		assertThat(aggregate.size(), is(1));
	}

	@Test
	public void testAddAll() throws Exception {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);

		// when
		final boolean added = aggregate.addAll(agents);

		// then
		assertThat(added, is(true));
		assertThat(aggregate.size(), is(2));
	}

	@Test
	public void testClear() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		aggregate.addAll(ImmutableList.of(agent1, agent2));

		// when
		aggregate.clear();

		// then
		assertThat(aggregate.isEmpty(), is(true));
	}

	@Test
	public void testContains() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final ISimpleAgent agentNotAdded = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		aggregate.addAll(agents);

		// when
		final boolean contains1 = aggregate.contains(agent1);
		final boolean contains2 = aggregate.contains(agent2);
		final boolean containsNotAdded = aggregate.contains(agentNotAdded);

		// then
		assertThat(contains1, is(true));
		assertThat(contains2, is(true));
		assertThat(containsNotAdded, is(false));
	}

	@Test
	public void testContainsAll() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final ISimpleAgent agentNotAdded = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		final List<ISimpleAgent> all = ImmutableList.of(agent1, agent2, agentNotAdded);
		aggregate.addAll(agents);

		// when
		final boolean contains1 = aggregate.containsAll(agents);
		final boolean containsNotAdded = aggregate.containsAll(all);

		// then
		assertThat(contains1, is(true));
		assertThat(containsNotAdded, is(false));
	}

	@Test
	public void testIsEmpty() {
		// then
		assertThat(aggregate.isEmpty(), is(true));
	}

	@Test
	public void testIsNotEmpty() {
		// given
		final ISimpleAgent agent = createMockAgentWithAddress();

		// when
		final boolean added = aggregate.add(agent);

		// then
		assertThat(added, is(true));
		assertThat(aggregate.isEmpty(), is(false));
	}

	@Test
	public void testIterator() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		aggregate.addAll(agents);

		// when
		final Iterator<ISimpleAgent> iterator = aggregate.iterator();

		// then
		final boolean condition = all(iterator, in(agents));
		assertThat(condition, is(true));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testReadOnlyIterator() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		aggregate.addAll(agents);

		// when
		final Iterator<ISimpleAgent> iterator = aggregate.iterator();
		iterator.remove();
	}

	@Test
	public void testRemove() {
		// given
		final ISimpleAgent agent = createMockAgentWithAddress();
		final boolean added = aggregate.add(agent);

		// when
		final boolean removed = aggregate.remove(agent);

		// then
		assertThat(added, is(true));
		assertThat(removed, is(true));
		assertThat(aggregate.isEmpty(), is(true));
	}

	@Test
	public void testRemoveAll() throws Exception {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		final boolean added = aggregate.addAll(agents);

		// when
		final boolean removed = aggregate.removeAll(agents);

		// then
		assertThat(added, is(true));
		assertThat(removed, is(true));
		assertThat(aggregate.isEmpty(), is(true));
	}

	@Test
	public void testRetainAll() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		final boolean added = aggregate.addAll(agents);

		// when
		final boolean retained = aggregate.retainAll(ImmutableList.of(agent1));

		// then
		assertThat(added, is(true));
		assertThat(retained, is(true));
		assertThat(aggregate.size(), is(1));
		assertThat(aggregate.contains(agent2), is(false));
	}

	@Test
	public void testToArray() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		final boolean added = aggregate.addAll(agents);

		// when
		final Object[] array = aggregate.toArray();

		// then
		assertThat(added, is(true));
		assertThat(array, is(notNullValue()));
		assertThat(array.length, is(2));
		assertThat(array, hasItemInArray((Object)agent1));
		assertThat(array, hasItemInArray((Object)agent2));
	}

	@Test
	public void testToTypedArray() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		final boolean added = aggregate.addAll(agents);

		// when
		final ISimpleAgent[] array = aggregate.toArray(new ISimpleAgent[0]);

		// then
		assertThat(added, is(true));
		assertThat(array, is(notNullValue()));
		assertThat(array.length, is(2));
		assertThat(array, hasItemInArray(agent1));
		assertThat(array, hasItemInArray(agent2));
	}

	@Test
	public void testGetAgentsAddresses() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		aggregate.addAll(agents);

		// when
		final Collection<AgentAddress> addresses = aggregate.getAgentsAddresses();

		// then
		assertThat(addresses, is(notNullValue()));
		assertThat(addresses.size(), is(2));
		assertThat(addresses, hasItem(agent1.getAddress()));
		assertThat(addresses, hasItem(agent2.getAddress()));
	}

	@Test
	public void testGetAgent() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		aggregate.addAll(agents);

		// when
		final ISimpleAgent agent = aggregate.getAgent(agent1.getAddress());

		// then
		assertThat(agent, is(notNullValue()));
		assertThat(agent, is(agent1));
	}

	@Test
	public void testContainsAgent() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final ISimpleAgent agent3 = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		aggregate.addAll(agents);

		// when
		final boolean contained1 = aggregate.containsAgent(agent1.getAddress());
		final boolean contained2 = aggregate.containsAgent(agent3.getAddress());

		// then
		assertThat(contained1, is(true));
		assertThat(contained2, is(false));
	}

	@Test
	public void testRemoveAgent() {
		// given
		final ISimpleAgent agent1 = createMockAgentWithAddress();
		final ISimpleAgent agent2 = createMockAgentWithAddress();
		final List<ISimpleAgent> agents = ImmutableList.of(agent1, agent2);
		aggregate.addAll(agents);

		// when
		aggregate.removeAgent(agent1.getAddress());

		// then
		assertThat(aggregate.contains(agent1), is(false));
		assertThat(aggregate.size(), is(1));
	}

	/**
	 * Test for AGE-152.
	 */
	@Test
	public void shouldBeAbleToAddAggregateToAggregate() {
		// given
		final SimpleAggregate simpleAggregate2 = new SimpleAggregate(mock(AgentAddress.class));
		simpleAggregate2.setActionService(actionService);

		// when
		final boolean added = aggregate.add(simpleAggregate2);

		// then
		assertThat(added, is(true));
		assertThat(aggregate.contains(simpleAggregate2), is(true));
	}
}
