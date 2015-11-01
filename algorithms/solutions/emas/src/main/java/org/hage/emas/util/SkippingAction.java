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
 * Created: 2012-03-22
 * $Id$
 */

package org.hage.emas.util;


import org.hage.agent.AgentException;
import org.hage.emas.agent.EmasAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility action to skip one action execution. Its purpose is to properly interleave migration action, so that they
 * execute together before all other action driven aggregates actions.
 * <p>
 * This class can be removed when proper interleaving of actions is implemented.
 *
 * @param <A> the type of agent to perform the action on
 * @author AGH AgE Team
 */
public class SkippingAction<A extends EmasAgent> extends ChainingAction<A> {

    private static final Logger log = LoggerFactory.getLogger(SkippingAction.class);

    @Override
    protected void doPerform(final A agent) throws AgentException {
        // do nothing
        log.debug("Performing skipping action on {}", agent);
    }
}
