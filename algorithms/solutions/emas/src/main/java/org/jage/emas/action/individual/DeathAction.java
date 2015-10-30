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
 * Created: 2012-01-30
 * $Id$
 */

package org.jage.emas.action.individual;


import org.jage.action.SingleAction;
import org.jage.action.context.KillAgentActionContext;
import org.jage.address.agent.AgentAddress;
import org.jage.agent.AgentException;
import org.jage.emas.agent.IndividualAgent;
import org.jage.emas.predicate.IPredicate;
import org.jage.emas.util.ChainingAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.jage.address.selector.Selectors.singleAddress;


/**
 * This action handler performs death actions on agent. Depending on some predicate, an agent may die.
 *
 * @author AGH AgE Team
 */
public final class DeathAction extends ChainingAction<IndividualAgent> {

    private static final Logger log = LoggerFactory.getLogger(DeathAction.class);

    @Inject
    private IPredicate<IndividualAgent> deathPredicate;

    @Override
    protected void doPerform(final IndividualAgent agent) throws AgentException {
        log.debug("Performing death action on {}", agent);

        if(deathPredicate.apply(agent)) {
            log.debug("Agent {} is dying.", agent);
            final AgentAddress agentAddress = agent.getAddress();
            final SingleAction deathAction = new SingleAction(singleAddress(agentAddress), new KillAgentActionContext());
            agent.getEnvironment().submitAction(deathAction);
        }
    }
}
