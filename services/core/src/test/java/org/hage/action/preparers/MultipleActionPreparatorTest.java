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
 * Created: 2011-05-06
 * $Id$
 */

package org.hage.action.preparers;


import com.google.common.collect.ImmutableList;
import org.hage.action.Action;
import org.hage.action.IActionContext;
import org.hage.action.SingleAction;
import org.hage.agent.IAgent;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Tests for {@link MultipleActionPreparatorTest}.
 *
 * @author AGH AgE Team
 */
public class MultipleActionPreparatorTest extends AbstractActionPreparatorTest {

    private final MultipleActionPreparer<IAgent> preparator = new MultipleActionPreparer<IAgent>();
    @Mock
    private IActionContext actionContext1;
    @Mock
    private IActionContext actionContext2;

    @Test
    public void testPrepareAction() {
        // given
        List<IActionContext> list = ImmutableList.of(actionContext1, actionContext2);
        preparator.setActionContexts(list);

        // when
        Collection<Action> actions = preparator.prepareActions(agent);

        // then
        assertEquals(2, actions.size());

        for(Action action : actions) {
            assertTrue(action instanceof SingleAction);
            SingleAction singleAction = (SingleAction) action;
            assertTrue(singleAction.getTarget().selects(agentAddress));
            assertTrue(list.contains(singleAction.getContext()));
        }
    }
}
