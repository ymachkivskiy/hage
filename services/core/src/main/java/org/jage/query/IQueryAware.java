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
 * Created: 2011-10-06
 * $Id$
 */

package org.jage.query;

/**
 * The interface for all classes that can and want to be notified about a query executed over them and alter its
 * behaviour.
 * 
 * @param <Q>
 *            A type of a queried object.
 * @param <R>
 *            A type of results.
 * @param <T>
 *            A type of the query that will be used against an implementing class.
 * 
 * @author AGH AgE Team
 */
public interface IQueryAware<Q, R, T extends IQuery<Q, R>> {

	/**
	 * Notifies that the query is starting to be executed. Called in the beginning of query operation, before performing
	 * any action.
	 * <p>
	 * The target can replace an object to be queried by returning non-<code>null</code> value from this method.
	 * However, the type of this value cannot be changed because the rest of the query is dependent on it.
	 * 
	 * @param query
	 *            The query executed over the implementing object.
	 * 
	 * @return Non-<code>null</code> value to replace a queried object, <code>null</code> if the query should continue
	 *         on the current target.
	 */
	Q beforeExecute(T query);

	/**
	 * Notifies that the query has finished its operation. It is called just before a return from the
	 * {@link IQuery#execute(Object)} method.
	 * 
	 * @param query
	 *            The query executed over the implementing object.
	 */
	void afterExecute(T query);

}
