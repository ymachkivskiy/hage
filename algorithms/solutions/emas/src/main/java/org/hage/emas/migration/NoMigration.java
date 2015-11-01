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
 * Created: 2012-03-16
 * $Id$
 */

package org.hage.emas.migration;


import org.hage.agent.AgentException;
import org.hage.agent.IAgent;
import org.hage.property.ClassPropertyContainer;


/**
 * No-migration strategy. Does nothing.
 *
 * @param <A> the type of agents
 * @author AGH AgE Team
 */
public class NoMigration<A extends IAgent> extends ClassPropertyContainer implements Migration<A> {

    @Override
    public void migrate(final A agent) throws AgentException {
        // do nothing
    }
}
