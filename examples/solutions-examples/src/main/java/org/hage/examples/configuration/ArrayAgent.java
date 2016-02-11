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
 * Created: 2011-07-11
 * $Id$
 */

package org.hage.examples.configuration;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.agent.AgentAddressSupplier;
import org.hage.platform.component.execution.agent.SimpleAgent;
import org.hage.platform.component.provider.IComponentInstanceProvider;
import org.hage.platform.component.provider.IComponentInstanceProviderAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


/**
 * This class provides a simple agent that presents how to obtain and handle arrays from the instance provider.
 *
 * @author AGH AgE Team
 */
public class ArrayAgent extends SimpleAgent implements IComponentInstanceProviderAware {

    private static final long serialVersionUID = 1L;

    private final Logger log = LoggerFactory.getLogger(ArrayAgent.class);
    private IComponentInstanceProvider instanceProvider;

    public ArrayAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public ArrayAgent(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    @Override
    public void init() {
        log.info("Initializing Hello World Simple Agent: {}", getAddress());

        final ExampleClass[] array = (ExampleClass[]) instanceProvider.getInstance("object-example");

        log.info("Obtained an instance of object-example: {}", array);
        log.info("Values:");
        for(final ExampleClass element : array) {
            log.info("Instance: {}. Value: {}", element, element.getArgument());
        }

        final Long[] longArray = (Long[]) instanceProvider.getInstance("long-example");

        log.info("Obtained an instance of long-example: {}", longArray);
        log.info("Values:");
        for(final long element : longArray) {
            log.info("Instance: {}", element);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation does nothing.
     *
     * @see org.hage.agent.AbstractAgent#finish()
     */
    @Override
    public boolean finish() {
        log.info("Finishing Array Agent: {}", getAddress());
        return true;
    }

    @Override
    public void step() {
        // Do nothing
    }

    @Override
    public void setInstanceProvider(final IComponentInstanceProvider provider) {
        this.instanceProvider = provider;
    }
}
