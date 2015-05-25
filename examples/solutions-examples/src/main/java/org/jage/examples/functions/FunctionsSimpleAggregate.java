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

package org.jage.examples.functions;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.SimpleAggregate;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.DuplicatePropertyNameException;
import org.jage.property.InvalidPropertyPathException;
import org.jage.property.functions.AvgFunction;
import org.jage.property.functions.CountFunction;
import org.jage.property.functions.MaxFunction;
import org.jage.property.functions.MinFunction;
import org.jage.property.functions.SumFunction;

public class FunctionsSimpleAggregate extends SimpleAggregate {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(FunctionsSimpleAggregate.class);

	public FunctionsSimpleAggregate(final AgentAddress address) {
		super(address);
	}

	@Inject
	public FunctionsSimpleAggregate(final AgentAddressSupplier supplier) {
		super(supplier);
	}

	public void init() throws ComponentException {
		try {
			addFunction(new CountFunction("count", "@Agents[*].value"));
			addFunction(new SumFunction("sum", "@Agents[*].value"));
			addFunction(new AvgFunction("avg", "@Agents[*].value", -1));
			addFunction(new MinFunction("min", "@Agents[*].value", -1));
			addFunction(new MaxFunction("max", "@Agents[*].value", -1));
		} catch (final DuplicatePropertyNameException e) {
			log.error("Could not create property function", e);
		}
	}

	//@Override
	public void step() {
		super.step();

		final String[] names = new String[] {"count", "sum", "avg", "min", "max"};
		final Object[] values = new Object[5];

		try {
			for (int i = 0; i < values.length; i++) {
				values[i] = getProperty(names[i]).getValue();
			}
		} catch (final InvalidPropertyPathException e) {
			log.error("Exception:", e);
		}

		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			builder.append(names[i] + "=" + values[i] + ", ");
		}
		log.info("{}: {}", this, builder.toString());
	}
}
