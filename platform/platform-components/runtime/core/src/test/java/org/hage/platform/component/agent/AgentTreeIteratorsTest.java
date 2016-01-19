package org.hage.platform.component.agent;
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

import com.google.common.collect.Iterators;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AgentTreeIteratorsTest {

    @Test(expected = NullPointerException.class)
    public void preOrderTreeShouldThrowExcOnNull() {
        AgentTreeIterators.preOrderTree(null);
    }

    @Test
    public void preOrderTreeShouldReturnSelfIfNotAggregate() {
        // given
        IAgent agent = mock(IAgent.class);

        // when
        Iterable<IAgent> tree = AgentTreeIterators.preOrderTree(agent);

        // then
        assertThat(tree, contains(agent));
    }

    @Test
    public void preOrderTreeShouldReturnSelfIfEmptyAggregate() {
        // given
        @SuppressWarnings("unchecked")
        IAggregate<IAgent> aggregate = mock(IAggregate.class);
        when(aggregate.iterator()).thenReturn(Iterators.<IAgent> emptyIterator());

        // when
        Iterable<IAgent> tree = AgentTreeIterators.preOrderTree(aggregate);

        // then
        assertThat(tree, contains((IAgent) aggregate));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void preOrderTreeShouldReturnPreOrderTree() {
        // given
        IAggregate<IAgent> root_1 = mock(IAggregate.class);
        IAgent root_1_1 = mock(IAgent.class);
        IAgent root_1_2 = mock(IAgent.class);
        when(root_1.iterator()).thenReturn(Iterators.forArray(root_1_1, root_1_2));

        IAggregate<IAgent> root_2 = mock(IAggregate.class);
        IAgent root_2_1 = mock(IAgent.class);
        IAgent root_2_2 = mock(IAgent.class);
        when(root_2.iterator()).thenReturn(Iterators.forArray(root_2_1, root_2_2));

        IAggregate<IAgent> root = mock(IAggregate.class);
        when(root.iterator()).thenReturn(Iterators.<IAgent> forArray(root_1, root_2));

        // when
        Iterable<IAgent> tree = AgentTreeIterators.preOrderTree(root);

        // then
        assertThat(tree, contains(root, root_1, root_1_1, root_1_2, root_2, root_2_1, root_2_2));
    }

    @Test(expected = NullPointerException.class)
    public void postOrderTreeShouldThrowExcOnNull() {
        AgentTreeIterators.postOrderTree(null);
    }

    @Test
    public void postOrderTreeShouldReturnSelfIfNotAggregate() {
        // given
        IAgent agent = mock(IAgent.class);

        // when
        Iterable<IAgent> tree = AgentTreeIterators.postOrderTree(agent);

        // then
        assertThat(tree, contains(agent));
    }

    @Test
    public void postOrderTreeShouldReturnSelfIfEmptyAggregate() {
        // given
        @SuppressWarnings("unchecked")
        IAggregate<IAgent> aggregate = mock(IAggregate.class);
        when(aggregate.iterator()).thenReturn(Iterators.<IAgent> emptyIterator());

        // when
        Iterable<IAgent> tree = AgentTreeIterators.postOrderTree(aggregate);

        // then
        assertThat(tree, contains((IAgent) aggregate));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void postOrderTreeShouldReturnPostOrderTree() {
        // given
        IAggregate<IAgent> root_1 = mock(IAggregate.class);
        IAgent root_1_1 = mock(IAgent.class);
        IAgent root_1_2 = mock(IAgent.class);
        when(root_1.iterator()).thenReturn(Iterators.forArray(root_1_1, root_1_2));

        IAggregate<IAgent> root_2 = mock(IAggregate.class);
        IAgent root_2_1 = mock(IAgent.class);
        IAgent root_2_2 = mock(IAgent.class);
        when(root_2.iterator()).thenReturn(Iterators.forArray(root_2_1, root_2_2));

        IAggregate<IAgent> root = mock(IAggregate.class);
        when(root.iterator()).thenReturn(Iterators.<IAgent> forArray(root_1, root_2));

        // when
        Iterable<IAgent> tree = AgentTreeIterators.postOrderTree(root);

        // then
        assertThat(tree, contains(root_1_1, root_1_2, root_1, root_2_1, root_2_2, root_2, root));
    }
}
