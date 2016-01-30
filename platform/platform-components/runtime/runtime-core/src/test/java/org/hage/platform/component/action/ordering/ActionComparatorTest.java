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
 * Created: 2012-03-29
 * $Id$
 */

package org.hage.platform.component.action.ordering;


import com.google.common.collect.ImmutableList;
import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.selector.AddressSelector;
import org.hage.platform.component.action.ActionException;
import org.hage.platform.component.action.IActionContext;
import org.hage.platform.component.action.SingleAction;
import org.hage.platform.component.action.ordering.ActionComparator.Relation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


/**
 * Tests for the {@link ActionComparator} class.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ActionComparatorTest {

    private ActionComparator comparator;

    private IActionContext firstContextInstance = new IActionContext() {/* Empty */

    };

    private Class<? extends IActionContext> firstContextClass = firstContextInstance.getClass();

    private IActionContext secondContextInstance = new IActionContext() {/* Empty */

    };

    private Class<? extends IActionContext> secondContextClass = secondContextInstance.getClass();

    private IActionContext thirdContextInstance = new IActionContext() {/* Empty */

    };

    private Class<? extends IActionContext> thirdContextClass = thirdContextInstance.getClass();

    @Mock
    private AddressSelector<AgentAddress> selector;

    @Before
    public void setUp() {
        comparator = new ActionComparator();
    }

    @Test
    public void testExplicitBefore() {
        // given
        // firstContextClass < secondContextClass
        comparator.addContextRelation(firstContextClass, secondContextClass, Relation.IS_BEFORE);
        SingleAction sa1 = new SingleAction(selector, firstContextInstance);
        SingleAction sa2 = new SingleAction(selector, secondContextInstance);

        // when
        final int results1to2 = comparator.compare(sa1, sa2);
        final int results2to1 = comparator.compare(sa2, sa1);

        // then
        assertThat(results1to2, is(lessThan(0)));
        assertThat(results1to2, is(equalTo(-results2to1)));
    }

    @Test
    public void testExplicitAfter() {
        // given
        // firstContextClass < secondContextClass
        comparator.addContextRelation(firstContextClass, secondContextClass, Relation.IS_AFTER);
        SingleAction sa1 = new SingleAction(selector, firstContextInstance);
        SingleAction sa2 = new SingleAction(selector, secondContextInstance);

        // when
        final int results1to2 = comparator.compare(sa1, sa2);
        final int results2to1 = comparator.compare(sa2, sa1);

        // then
        assertThat(results1to2, is(greaterThan(0)));
        assertThat(results1to2, is(equalTo(-results2to1)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOrdered() {
        // given
        // firstContextClass < secondContextClass < thirdContextClass
        comparator.addContextsAsOrdered(firstContextClass, secondContextClass, thirdContextClass);
        SingleAction sa1 = new SingleAction(selector, firstContextInstance);
        SingleAction sa2 = new SingleAction(selector, secondContextInstance);
        SingleAction sa3 = new SingleAction(selector, thirdContextInstance);

        // when
        final int results1to2 = comparator.compare(sa1, sa2);
        final int results1to3 = comparator.compare(sa1, sa3);

        final int results2to1 = comparator.compare(sa2, sa1);
        final int results2to3 = comparator.compare(sa2, sa3);

        final int results3to1 = comparator.compare(sa3, sa1);
        final int results3to2 = comparator.compare(sa3, sa2);

        // then
        assertThat(results1to2, is(lessThan(0)));
        assertThat(results1to2, is(equalTo(-results2to1)));

        assertThat(results1to3, is(lessThan(0)));
        assertThat(results1to3, is(equalTo(-results3to1)));

        assertThat(results2to3, is(lessThan(0)));
        assertThat(results2to3, is(equalTo(-results3to2)));
    }

    @Test
    public void testOrderedList() {
        // given
        // firstContextClass < secondContextClass < thirdContextClass
        List<Class<? extends IActionContext>> list = ImmutableList.of(firstContextClass, secondContextClass,
                                                                      thirdContextClass);
        comparator.addContextsAsOrdered(list);
        SingleAction sa1 = new SingleAction(selector, firstContextInstance);
        SingleAction sa2 = new SingleAction(selector, secondContextInstance);
        SingleAction sa3 = new SingleAction(selector, thirdContextInstance);

        // when
        final int results1to2 = comparator.compare(sa1, sa2);
        final int results1to3 = comparator.compare(sa1, sa3);

        final int results2to1 = comparator.compare(sa2, sa1);
        final int results2to3 = comparator.compare(sa2, sa3);

        final int results3to1 = comparator.compare(sa3, sa1);
        final int results3to2 = comparator.compare(sa3, sa2);

        // then
        assertThat(results1to2, is(lessThan(0)));
        assertThat(results1to2, is(equalTo(-results2to1)));

        assertThat(results1to3, is(lessThan(0)));
        assertThat(results1to3, is(equalTo(-results3to1)));

        assertThat(results2to3, is(lessThan(0)));
        assertThat(results2to3, is(equalTo(-results3to2)));
    }

    @Test
    public void testAlwaysFirst() {
        // given
        // firstContextClass < *
        comparator.addAlwaysFirstContext(firstContextClass);
        SingleAction sa1 = new SingleAction(selector, firstContextInstance);
        SingleAction sa2 = new SingleAction(selector, secondContextInstance);
        SingleAction sa3 = new SingleAction(selector, thirdContextInstance);

        // when
        final int results1to2 = comparator.compare(sa1, sa2);
        final int results2to1 = comparator.compare(sa2, sa1);

        final int results1to3 = comparator.compare(sa1, sa3);
        final int results3to1 = comparator.compare(sa3, sa1);

        // then
        assertThat(results1to2, is(lessThan(0)));
        assertThat(results1to2, is(equalTo(-results2to1)));

        assertThat(results1to3, is(lessThan(0)));
        assertThat(results1to3, is(equalTo(-results3to1)));
    }

    @Test
    public void testAlwaysLast() {
        // given
        // * < firstContextClass
        comparator.addAlwaysLastContext(firstContextClass);
        SingleAction sa1 = new SingleAction(selector, firstContextInstance);
        SingleAction sa2 = new SingleAction(selector, secondContextInstance);
        SingleAction sa3 = new SingleAction(selector, thirdContextInstance);

        // when
        final int results1to2 = comparator.compare(sa1, sa2);
        final int results2to1 = comparator.compare(sa2, sa1);

        final int results1to3 = comparator.compare(sa1, sa3);
        final int results3to1 = comparator.compare(sa3, sa1);

        // then
        assertThat(results1to2, is(greaterThan(0)));
        assertThat(results1to2, is(equalTo(-results2to1)));

        assertThat(results1to3, is(greaterThan(0)));
        assertThat(results1to3, is(equalTo(-results3to1)));
    }

    @Test
    public void testImplicitTransitivity1() {
        // given
        // firstContextClass < secondContextClass
        // AND secondContextClass < thirdContextClass
        // => firstContextClass < thirdContextClass
        comparator.addContextRelation(firstContextClass, secondContextClass, Relation.IS_BEFORE);
        comparator.addContextRelation(secondContextClass, thirdContextClass, Relation.IS_BEFORE);
        SingleAction sa1 = new SingleAction(selector, firstContextInstance);
        SingleAction sa3 = new SingleAction(selector, thirdContextInstance);

        // when
        final int results1to3 = comparator.compare(sa1, sa3);
        final int results3to1 = comparator.compare(sa3, sa1);

        // then
        assertThat(results1to3, is(lessThan(0)));
        assertThat(results1to3, is(equalTo(-results3to1)));
    }

    @Test
    public void testImplicitTransitivity2() {
        // given
        // secondContextClass < thirdContextClass
        // AND firstContextClass < secondContextClass
        // => firstContextClass < thirdContextClass
        comparator.addContextRelation(secondContextClass, thirdContextClass, Relation.IS_BEFORE);
        comparator.addContextRelation(firstContextClass, secondContextClass, Relation.IS_BEFORE);
        SingleAction sa1 = new SingleAction(selector, firstContextInstance);
        SingleAction sa3 = new SingleAction(selector, thirdContextInstance);

        // when
        final int results1to3 = comparator.compare(sa1, sa3);
        final int results3to1 = comparator.compare(sa3, sa1);

        // then
        assertThat(results1to3, is(lessThan(0)));
        assertThat(results1to3, is(equalTo(-results3to1)));
    }

    @Test
    public void testNoRelation() {
        // given
        // firstContextClass < secondContextClass
        comparator.addContextRelation(firstContextClass, secondContextClass, Relation.IS_BEFORE);
        SingleAction sa1 = new SingleAction(selector, firstContextInstance);
        SingleAction sa3 = new SingleAction(selector, thirdContextInstance);

        // when
        final int results1to3 = comparator.compare(sa1, sa3);
        final int results3to1 = comparator.compare(sa3, sa1);

        // then
        assertThat(results1to3, is(equalTo(0)));
        assertThat(results3to1, is(equalTo(0)));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = ActionException.class)
    public void testExceptionWhenContextReconfigured() {
        // when
        comparator.addAlwaysFirstContext(firstContextClass);
        comparator.addContextsAsOrdered(firstContextClass, secondContextClass);
    }
}
