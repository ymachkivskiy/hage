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
 * Created: 2012-04-07
 * $Id$
 */

package org.jage.agent;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.query.AgentEnvironmentQuery;

/**
 * The query service for the default {@link SimpleAggregate}.
 * 
 * @since 2.6
 * @author AGH AgE Team
 */
public class AggregateQueryService {

	private static final Logger log = LoggerFactory.getLogger(AggregateQueryService.class);

	private SimpleAggregate aggregate;

	/**
	 * Executes a query in the parent environment.
	 * 
	 * @param query
	 *            the query to execute.
	 * @return results of the query.
	 * @throws AgentException
	 *             if the environment is not available.
	 */
	public <E extends IAgent, T> Collection<T> queryParent(final AgentEnvironmentQuery<E, T> query) {
		return aggregate.queryEnvironment(query);
	}

	/**
	 * Sets the aggregate that owns this service instance.
	 * 
	 * @param aggregate
	 *            the owner.
	 */
	public void setAggregate(final SimpleAggregate aggregate) {
		this.aggregate = aggregate;
	}
}
