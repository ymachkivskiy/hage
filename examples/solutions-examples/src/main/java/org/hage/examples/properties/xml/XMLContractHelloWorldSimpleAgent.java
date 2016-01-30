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

package org.hage.examples.properties.xml;


import org.hage.platform.communication.address.agent.AgentAddress;
import org.hage.platform.communication.address.agent.AgentAddressSupplier;
import org.hage.platform.component.agent.SimpleAgent;
import org.hage.platform.component.exception.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


/**
 * This agent presents a usage of components described with XML.
 *
 * @author AGH AgE Team
 */
public class XMLContractHelloWorldSimpleAgent extends SimpleAgent {

    private static final long serialVersionUID = -7280785631107779790L;

    private static final Logger log = LoggerFactory.getLogger(XMLContractHelloWorldSimpleAgent.class);

    @Inject
    private ExampleComponent exampleComponent;

    public XMLContractHelloWorldSimpleAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public XMLContractHelloWorldSimpleAgent(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    public void setExampleComponent(final ExampleComponent exampleComponent) {
        this.exampleComponent = exampleComponent;
    }

    @Override
    public void step() {
        log.info("Hello world! An example not-ClassPropertyContainer object will introduce himself: ");
        exampleComponent.printComponentInfo();
        try {
            Thread.sleep(1000);
        } catch(final InterruptedException ex) {
            log.error("Interrupted", ex);
        }
    }

    @Override
    public void init() throws ComponentException {
        super.init();
        log.info("Initializing Hello World Simple Agent with XMLContracts.");
    }

    @Override
    public boolean finish() {
        log.info("Finishing Hello World Simple Agent with XMLContracts.");
        return true;
    }
}
