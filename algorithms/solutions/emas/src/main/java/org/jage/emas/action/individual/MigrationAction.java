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


import org.jage.agent.AgentException;
import org.jage.emas.agent.IndividualAgent;
import org.jage.emas.migration.Migration;
import org.jage.emas.predicate.IPredicate;
import org.jage.emas.util.ChainingAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


/**
 * A migration action handler. If a migration predicate applies to some agent, it is migrated according to some
 * migration strategy.
 *
 * @author AGH AgE Team
 */
public final class MigrationAction extends ChainingAction<IndividualAgent> {

    private static final Logger log = LoggerFactory.getLogger(MigrationAction.class);

    @Inject
    private IPredicate<IndividualAgent> migrationPredicate;

    @Inject
    private Migration<IndividualAgent> migrationStrategy;

    @Override
    public void doPerform(final IndividualAgent agent) throws AgentException {
        log.debug("Performing migration action on {}", agent);
        if(migrationPredicate.apply(agent)) {
            migrationStrategy.migrate(agent);
        }
    }
}
