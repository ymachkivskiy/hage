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
import org.hage.platform.component.agent.SimpleAgent;
import org.hage.platform.component.exception.ComponentException;
import org.hage.platform.component.provider.IComponentInstanceProvider;
import org.hage.platform.component.provider.IComponentInstanceProviderAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;


/**
 * This class provides a simple agent that presents how to obtain and handle lists from the instance provider.
 *
 * @author AGH AgE Team
 */
public class ListAgent extends SimpleAgent implements IComponentInstanceProviderAware {

    private static final long serialVersionUID = 1L;

    private final Logger log = LoggerFactory.getLogger(ListAgent.class);

    @Inject
    private List<ExampleClass> injectedList;
    private IComponentInstanceProvider instanceProvider;

    public ListAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public ListAgent(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() throws ComponentException {
        super.init();
        log.info("Initializing List Simple Agent: {}", getAddress());

        // Print the injected list
        log.info("The instance was injected: {}", injectedList);
        log.info("Values:");
        for(final ExampleClass element : injectedList) {
            log.info("Instance: {}", element);
        }

        // Retrieve and print the "object-example" list
        final List<ExampleClass> list = (List<ExampleClass>) instanceProvider.getInstance("object-example");
        log.info("Obtained an instance of object-example: {}", list);
        log.info("Values:");
        for(final ExampleClass element : list) {
            log.info("Instance: {}", element);
        }
    }

    @Override
    public boolean finish() {
        log.info("Finishing List Agent: {}", getAddress());
        return true;
    }

    @Override
    public void step() {
        // Empty
    }

    @Override
    public void setInstanceProvider(final IComponentInstanceProvider provider) {
        this.instanceProvider = provider;
    }
}
