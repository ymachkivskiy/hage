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
 * Created: 2012-04-20
 * $Id$
 */

package org.hage.examples.functions;


import org.hage.address.agent.AgentAddress;
import org.hage.address.agent.AgentAddressSupplier;
import org.hage.platform.component.agent.SimpleAgent;
import org.hage.property.InvalidPropertyOperationException;
import org.hage.property.InvalidPropertyPathException;
import org.hage.property.Property;
import org.hage.property.PropertyField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Random;


public class FunctionsSimpleAgent extends SimpleAgent {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(FunctionsSimpleAgent.class);
    private static final Random RAND = new Random();
    @PropertyField(propertyName = Properties.VALUE)
    private Double value;

    public FunctionsSimpleAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public FunctionsSimpleAgent(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    @Override
    public void step() {
        try {
            final Property property = getProperty(Properties.VALUE);
            property.setValue(RAND.nextDouble());
        } catch(final InvalidPropertyPathException e) {
            log.error("Can't access value property", e);
        } catch(final InvalidPropertyOperationException e) {
            log.error("Can't set value property", e);
        }

        log.info("{}: value is {}", this, value);
    }


    public static class Properties {

        public static final String VALUE = "value";
    }
}
