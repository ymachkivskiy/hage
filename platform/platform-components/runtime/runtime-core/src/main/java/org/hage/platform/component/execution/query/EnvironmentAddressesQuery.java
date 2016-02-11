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
 * Created: 2013-12-22
 * $Id$
 */

package org.hage.platform.component.execution.query;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.component.execution.agent.IAgent;


public class EnvironmentAddressesQuery extends AgentEnvironmentQuery<IAgent, AgentAddress> {

    public EnvironmentAddressesQuery() {
        select(ValueSelectors.agentAddress()).process(QueryFunctions.<AgentAddress> flatten());
    }
}
