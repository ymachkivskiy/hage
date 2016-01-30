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
 * Created: 2011-04-29
 * $Id$
 */

package org.hage.platform.component.action.preparers;


import org.hage.platform.component.action.Action;
import org.hage.platform.component.agent.IAgent;

import java.util.List;


/**
 * A factory strategy being used by an ActionDrivenAgent. Given a set of properties, reflecting the calling agent state,
 * and a queryable interface, reflecting the calling agent's environment, it prepares a list of Action objects, to be
 * executed by the agent.
 * <p>
 * <p>
 * These actions encapsulate the actual behavior of the Agent. They may be either simple or complex.
 *
 * @param <T> a type of the agent that the preparator operates on.
 * @author AGH AgE Team
 */
public interface IActionPreparer<T extends IAgent> {

    /**
     * Returns the list of actions to be executed, given the state and environment of an agent.
     *
     * @param agent an agent whose state will be used.
     * @return the list of actions to be executed.
     */
    List<Action> prepareActions(T agent);
}
