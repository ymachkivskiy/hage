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
 * Created: 2011-04-07
 * $Id$
 */

package org.hage.examples.delegation;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.agent.AgentAddressSupplier;
import org.hage.platform.component.execution.agent.SimpleAgent;
import org.hage.platform.component.provider.IComponentInstanceProvider;
import org.hage.platform.component.provider.IComponentInstanceProviderAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


/**
 * This agent presents an example delegation of strategies. It is a leaf of tree of agents. It can accept an echo
 * strategy coming from a parent.
 *
 * @author AGH AgE Team
 */
public class DelegationSimpleLeaf extends SimpleAgent implements IEchoStrategyAcceptingAgent,
                                                                 IComponentInstanceProviderAware {

    private static final long serialVersionUID = 2L;

    private static final Logger log = LoggerFactory.getLogger(DelegationSimpleLeaf.class);

    private IEchoStrategy echoStrategy;
    private IComponentInstanceProvider instanceProvider;


    public DelegationSimpleLeaf(final AgentAddress address) {
        super(address);
    }

    @Inject
    public DelegationSimpleLeaf(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    /**
     * Executes a step of the agent. This agent delegates its execution to the <em>echo strategy</em> and then sleeps
     * for a while. {@inheritDoc}
     *
     * @see org.hage.agent.SimpleAgent#step()
     */
    @Override
    public void step() {
        log.info("{} says Hello World from {}", getAddress().getFriendlyName(), getParentAddress().getFriendlyName());

        echoStrategy.echo(getParentAddress().toString());

        try {
            Thread.sleep(200);
        } catch(final InterruptedException e) {
            log.error("Interrupted", e);
        }
    }

    @Override
    public boolean finish() {
        log.info("Finishing Delegation Simple Leaf: {}", getAddress().getFriendlyName());
        return true;
    }

    @Override
    public void acceptEchoStrategy(final String echoStrategyName) {
        log.info("{} asked to accept a strategy {}", getAddress().getFriendlyName(), echoStrategyName);

        echoStrategy = (IEchoStrategy) instanceProvider.getInstance(echoStrategyName);
    }

    @Override
    public void setInstanceProvider(final IComponentInstanceProvider provider) {
        this.instanceProvider = provider;
    }
}
