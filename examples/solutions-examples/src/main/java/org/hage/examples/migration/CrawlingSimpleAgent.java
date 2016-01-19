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
 * Created: 2011-03-15
 * $Id$
 */

package org.hage.examples.migration;


import org.hage.address.agent.AgentAddress;
import org.hage.address.agent.AgentAddressSupplier;
import org.hage.platform.component.action.AgentActions;
import org.hage.platform.component.agent.AgentException;
import org.hage.platform.component.agent.SimpleAgent;
import org.hage.platform.component.query.EnvironmentAddressesQuery;
import org.hage.property.InvalidPropertyPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Random;


/**
 * This agent finds environments where it can migrate to. If there is any suitable in migrates to a random one every
 * few
 * seconds.
 *
 * @author AGH AgE Team
 */
public class CrawlingSimpleAgent extends SimpleAgent {

    private static final long serialVersionUID = 3L;
    private final Logger log = LoggerFactory.getLogger(CrawlingSimpleAgent.class);
    /**
     * Random number generator
     */
    private final Random random = new Random();
    /**
     * Steps step
     */
    protected int step = 0;
    private int migrationCount = 0;

    public CrawlingSimpleAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public CrawlingSimpleAgent(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    @Override
    public void step() {
        step++;
        if((step + hashCode()) % 50 == 0) {
            considerMigration();
        }

        try {
            Thread.sleep(10);
        } catch(final InterruptedException e) {
            log.error("Interrupted", e);
        }
    }

    /**
     * Considers migration and migrates randomly.
     */
    protected void considerMigration() {
        final Collection<AgentAddress> answer;
        AgentAddress target = null;
        try {
            log.info("Querying parent...");
            final EnvironmentAddressesQuery query = new EnvironmentAddressesQuery();

            answer = queryParentEnvironment(query);
            if(answer.size() > 1) {
                log.info("Agent: {} can migrate from {} to following environments:", getAddress().getFriendlyName(),
                         getParentAddress().getFriendlyName());
                float max = 0;
                for(final AgentAddress possibleTargetAddress : answer) {
                    if(!possibleTargetAddress.equals(getParentAddress())) {
                        log.info("   {}", possibleTargetAddress);
                    }
                    final float rand = random.nextFloat();
                    if(max < rand) {
                        max = rand;
                        target = possibleTargetAddress;
                    }
                }
            } else {
                log.info("Agent: {} can not migrate anywhere from: {}", getAddress().getFriendlyName(),
                         getParentAddress().getFriendlyName());
            }

            if(target != null) {
                if(!target.equals(getParentAddress())) {
                    log.info("Agent: {} decides to migrate to environment: {}", getAddress().getFriendlyName(), target);
                    try {
                        doAction(AgentActions.migrate(this, target));
                        migrationCount++;
                    } catch(final AgentException e) {
                        log.error("Can't move to: {}.", target, e);
                    }
                } else {
                    log.info("Agent: {} decides to stay in environment: {}", getAddress().getFriendlyName(), target);
                }
            }
        } catch(final AgentException e) {
            log.error("Agent exception", e);
        } catch(final InvalidPropertyPathException e) {
            log.error("Invalid property", e);
        }
    }

    @Override
    public boolean finish() {
        log.info("{}: Finishing.", getAddress().getFriendlyName());
        log.info("{}: {} steps.", getAddress().getFriendlyName(), step);
        log.info("{}: Migrated {} times.", getAddress().getFriendlyName(), migrationCount);
        return true;
    }

    public int getStep() {
        return step;
    }

    public int getMigrationCount() {
        return migrationCount;
    }
}
