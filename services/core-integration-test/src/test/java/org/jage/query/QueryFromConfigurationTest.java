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
 * Created: 2011-09-19
 * $Id$
 */

package org.jage.query;


import org.jage.address.agent.AgentAddress;
import org.jage.agent.AggregateActionService;
import org.jage.agent.AggregateQueryService;
import org.jage.platform.component.provider.IComponentInstanceProvider;
import org.jage.property.IPropertiesSet;
import org.jage.workplace.SimpleWorkplace;
import org.jage.workplace.WorkplaceEnvironment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.jage.query.ValueFilters.*;
import static org.mockito.Mockito.mock;


/**
 * Simple tests for querying agents in aggregate.
 *
 * @author AGH AgE Team
 */
@SuppressWarnings("unchecked")
public class QueryFromConfigurationTest {

    private SimpleWorkplace workplace;

    private PlainAggregate aggregate;

    private PlainAgent agent1;

    private PlainAgent agent2;

    private PlainAgent agent3;

    private IComponentInstanceProvider componentInstanceProvider;

    @Before
    public void setUp() throws Exception {
        // Configure mocks
        componentInstanceProvider = mock(IComponentInstanceProvider.class);

        // Configure agents
        workplace = new SimpleWorkplace();
        final WorkplaceEnvironment workplaceEnvironment = mock(WorkplaceEnvironment.class);
        // when(workplaceEnvironment.registerAgentInPath(Mockito.any(IAgentAddress.class),
        // Mockito.anyList())).thenReturn(true);
        workplace.setEnvironment(workplaceEnvironment);

        aggregate = new PlainAggregate(mock(AgentAddress.class));
        aggregate.setInstanceProvider(componentInstanceProvider);
        aggregate.setActionService(mock(AggregateActionService.class));
        aggregate.setQueryService(mock(AggregateQueryService.class));
        aggregate.init();
        aggregate.setAgentEnvironment(workplace);

        // agent 1
        agent1 = new PlainAgent(mock(AgentAddress.class));
        agent1.setWidth(1.0f);
        agent1.setHeight(4.5f);
        agent1.setDescription("my new description");
        agent1.setWeight(55.0);
        agent1.setColor(Color.RED);
        agent1.setDynamicFactor((short) 1000);
        agent1.setStaticFactor(500);
        agent1.setMode((byte) 30);
        agent1.setUpperLimit(new BigInteger("9999999999999"));
        agent1.init();

        aggregate.add(agent1);

        // agent 2
        agent2 = new PlainAgent(mock(AgentAddress.class));
        agent2.setWidth(1.5f);
        agent2.setHeight(3.2f);
        agent2.setDescription("hello world");
        agent2.setWeight(95.0);
        agent2.setColor(Color.BLACK);
        agent2.setDynamicFactor((short) 5000);
        agent2.setStaticFactor(50);
        agent2.setMode((byte) 12);
        agent2.setUpperLimit(new BigInteger("100"));
        agent2.init();

        aggregate.add(agent2);

        // agent 3
        agent3 = new PlainAgent(mock(AgentAddress.class));
        agent3.setWidth(2.45f);
        agent3.setHeight(3.9f);
        agent3.setDescription("simple description");
        agent3.setWeight(150.0);
        agent3.setColor(Color.BLUE);
        agent3.setDynamicFactor((short) 50);
        agent3.setStaticFactor(5000);
        agent3.setMode((byte) 1);
        agent3.setUpperLimit(new BigInteger("300000000"));
        agent3.init();

        aggregate.add(agent3);

        final List<AgentAddress> addresses = new LinkedList<AgentAddress>();
        addresses.add(agent1.getAddress());
        addresses.add(agent2.getAddress());
        addresses.add(agent3.getAddress());
    }

    /**
     * Tests an execution of the empty query.
     */
    @Test
    public void testEmptyQuery() {
        final AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
        final Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
        Assert.assertEquals(3, result.size());

        final Map<AgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);

        Assert.assertTrue(entries.containsKey(agent1.getAddress()));
        Assert.assertTrue(entries.containsKey(agent2.getAddress()));
        Assert.assertTrue(entries.containsKey(agent3.getAddress()));

        final IPropertiesSet agent1Props = entries.get(agent1.getAddress());
        Assert.assertNotNull(agent1Props);

        Assert.assertEquals(agent1Props.getProperty("width").getValue(), new Float(agent1.getWidth()));

        Assert.assertEquals(agent1Props.getProperty("height").getValue(), new Float(agent1.getHeight()));
        Assert.assertEquals(agent1Props.getProperty("description").getValue(), agent1.getDescription());

        final IPropertiesSet agent2Props = entries.get(agent2.getAddress());
        Assert.assertNotNull(agent2Props);

        Assert.assertEquals(agent2Props.getProperty("width").getValue(), new Float(agent2.getWidth()));
        Assert.assertEquals(agent2Props.getProperty("height").getValue(), new Float(agent2.getHeight()));
        Assert.assertEquals(agent2Props.getProperty("description").getValue(), agent2.getDescription());

        final IPropertiesSet agent3Props = entries.get(agent3.getAddress());
        Assert.assertNotNull(agent3Props);

        Assert.assertEquals(agent3Props.getProperty("width").getValue(), new Float(agent3.getWidth()));
        Assert.assertEquals(agent3Props.getProperty("height").getValue(), new Float(agent3.getHeight()));
        Assert.assertEquals(agent3Props.getProperty("description").getValue(), agent3.getDescription());

    }

    private static Map<AgentAddress, IPropertiesSet> getPropertyContainerMap(final Collection<PlainAgent> result) {
        final Map<AgentAddress, IPropertiesSet> entries = new HashMap<AgentAddress, IPropertiesSet>();
        for(final PlainAgent entry : result) {
            entries.put(entry.getAddress(), entry.getProperties());
        }
        return entries;
    }

    @Test
    public void testRangeConstraint1() {
        final AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
        generalPurposeQuery.matching("height", allOf(moreThan((float) 3.0), lessThan((float) 4.0)));
        final Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
        Assert.assertEquals(result.size(), 2);

        final Map<AgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);
        Assert.assertTrue(entries.containsKey(agent2.getAddress()));
        Assert.assertTrue(entries.containsKey(agent3.getAddress()));
    }

    @Test
    public void testRangeConstraint2() {
        final AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
        generalPurposeQuery.matching("staticFactor", allOf(moreOrEqual(50), lessOrEqual(5000)));
        final Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
        Assert.assertEquals(result.size(), 3);

        final Map<AgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);
        Assert.assertTrue(entries.containsKey(agent1.getAddress()));
        Assert.assertTrue(entries.containsKey(agent2.getAddress()));
        Assert.assertTrue(entries.containsKey(agent3.getAddress()));
    }

    @Test
    public void testRangeConstraint3() {
        final AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
        generalPurposeQuery.matching("staticFactor", allOf(moreOrEqual(50), lessThan(5000)));
        final Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
        Assert.assertEquals(result.size(), 2);

        final Map<AgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);
        Assert.assertTrue(entries.containsKey(agent1.getAddress()));
        Assert.assertTrue(entries.containsKey(agent2.getAddress()));

        final AgentEnvironmentQuery<PlainAgent, PlainAgent> queryEdge = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
        queryEdge.matching("staticFactor", allOf(moreThan(50), lessThan(5000)));
        final Collection<PlainAgent> resultEdge = queryEdge.execute(aggregate);
        Assert.assertEquals(resultEdge.size(), 1);

        final Map<AgentAddress, IPropertiesSet> entriesEdge = getPropertyContainerMap(result);
        Assert.assertTrue(entriesEdge.containsKey(agent1.getAddress()));

        final AgentEnvironmentQuery<PlainAgent, PlainAgent> queryEdge2 = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
        queryEdge2.matching("staticFactor", allOf(moreThan(50), lessThan(500)));
        final Collection<PlainAgent> resultEdge2 = queryEdge2.execute(aggregate);
        Assert.assertEquals(resultEdge2.size(), 0);
    }

    @Test
    public void testPatternConstraint() {
        final AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
        generalPurposeQuery.matching("description", pattern(".*new.*"));
        final Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
        Assert.assertEquals(result.size(), 1);

        final Map<AgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);
        Assert.assertTrue(entries.containsKey(agent1.getAddress()));

        final AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery2 = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
        generalPurposeQuery2.matching("description", pattern("hello world"));
        final Collection<PlainAgent> result2 = generalPurposeQuery2.execute(aggregate);
        Assert.assertEquals(result2.size(), 1);

        final Map<AgentAddress, IPropertiesSet> entries2 = getPropertyContainerMap(result2);
        Assert.assertTrue(entries2.containsKey(agent2.getAddress()));
    }

    @Test
    public void testValueConstraint() {
        final AgentEnvironmentQuery<PlainAgent, PlainAgent> generalPurposeQuery = new AgentEnvironmentQuery<PlainAgent, PlainAgent>();
        generalPurposeQuery.matching("upperLimit", ValueFilters.eq(new BigInteger("100")));
        final Collection<PlainAgent> result = generalPurposeQuery.execute(aggregate);
        Assert.assertEquals(1, result.size());

        final Map<AgentAddress, IPropertiesSet> entries = getPropertyContainerMap(result);
        Assert.assertTrue(entries.containsKey(agent2.getAddress()));
    }
}
