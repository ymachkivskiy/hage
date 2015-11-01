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

package org.hage.examples.helloworld;


import org.hage.address.agent.AgentAddress;
import org.hage.address.agent.AgentAddressSupplier;
import org.hage.agent.SimpleAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


/**
 * This agent only logs basic information. It doesn't perform any other operations.
 * <p>
 * Make sure that logger configuration allows to log at info level from this class.
 *
 * @author AGH AgE Team
 */
public class HelloWorldSimpleAgent extends SimpleAgent {

    private static final long serialVersionUID = 3L;

    private final Logger log = LoggerFactory.getLogger(HelloWorldSimpleAgent.class);

    public HelloWorldSimpleAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public HelloWorldSimpleAgent(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    /**
     * In this method, the agent performs its work.
     * <p>
     * {@inheritDoc}
     *
     * @see org.hage.agent.SimpleAgent#step()
     */
    @Override
    public void step() {
        log.info("{} says Hello World from {}.", getAddress().getFriendlyName(), getParentAddress().getFriendlyName());
        try {
            Thread.sleep(200);
        } catch(final InterruptedException e) {
            log.error("Interrupted", e);
        }
    }

    /**
     * This method is called when the agent is to be removed (e.g. when the system is shutting down).
     * <p>
     * {@inheritDoc}
     *
     * @see org.hage.agent.AbstractAgent#finish()
     */
    @Override
    public boolean finish() {
        log.info("Finishing Hello World Simple Agent: {}.", getAddress().getFriendlyName());
        return true;
    }

}
