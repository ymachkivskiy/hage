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
 * Created: 2012-04-09
 * $Id$
 */

package org.hage.utils;


import org.hage.address.agent.AgentAddress;
import org.hage.agent.ISimpleAgent;
import org.hage.agent.SimpleAgent;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * Test utilities related to agents.
 *
 * @author AGH AgE Team
 */
public final class AgentTestUtils {

    private AgentTestUtils() {
        // Empty
    }

    public static ISimpleAgent createMockAgentWithAddress() {
        return createMockAgentWithAddress(mock(AgentAddress.class));
    }

    public static ISimpleAgent createMockAgentWithAddress(final AgentAddress address) {
        final ISimpleAgent simpleAgent = mock(ISimpleAgent.class);
        given(simpleAgent.getAddress()).willReturn(address);
        return simpleAgent;
    }

    public static SimpleAgent createSimpleAgentWithoutStep() {
        return new SimpleAgent(mock(AgentAddress.class)) {

            private static final long serialVersionUID = 1L;

            @Override
            public void step() {
                // Empty
            }
        };
    }
}
