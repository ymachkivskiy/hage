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

package org.hage.emas.migration;


import com.google.common.base.Predicate;
import org.hage.action.AgentActions;
import org.hage.agent.AgentException;
import org.hage.agent.IAgent;
import org.hage.agent.IAgentEnvironment;
import org.hage.emas.agent.IndividualAgent;
import org.hage.query.AgentEnvironmentQuery;
import org.hage.random.IIntRandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;


/**
 * Random destination migration strategy. Chooses a destination at random from the agent's parent's siblings.
 *
 * @author AGH AgE Team
 */
public class RandomDestinationMigration implements Migration<IndividualAgent> {

    private static final Logger log = LoggerFactory.getLogger(RandomDestinationMigration.class);

    @Inject
    private IIntRandomGenerator rand;

    @Override
    public void migrate(final IndividualAgent agent) throws AgentException {
        final List<IAgent> islands = getSurroundingIslands(agent.getEnvironment());
        if(!islands.isEmpty()) {
            final IAgent destination = islands.get(rand.nextInt(islands.size()));
            doMigrate(agent, destination);
            log.debug("Migrating {} to {}", agent, destination);
        }
    }

    private List<IAgent> getSurroundingIslands(final IAgentEnvironment environment) {
        final Collection<IAgent> islands = environment.queryParent(new AgentEnvironmentQuery<IAgent, IAgent>());
        final Predicate<IAgent> predicate = new Predicate<IAgent>() {

            @Override
            public boolean apply(final IAgent input) {
                return !input.getAddress().equals(environment.getAddress());
            }
        };
        return newArrayList(filter(islands, predicate));
    }

    private void doMigrate(final IndividualAgent agent, final IAgent destination) throws AgentException {
        agent.getEnvironment().submitAction(AgentActions.migrate(agent, destination.getAddress()));
    }
}
