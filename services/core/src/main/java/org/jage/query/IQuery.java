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
 * Created: 2011-09-12
 * $Id$
 */

package org.jage.query;


import javax.annotation.Nonnull;


/**
 * The base interface for queries.
 * <p>
 * A query is defined as an action performed on a target object that leads to creation of query results. Concrete
 * implementations of queries need to provide following semantics:
 * <ul>
 * <li>a type of a target object (an object the query is performed on),
 * <li>a type of results (usually some kind of a collection),
 * <li>a relation between a target and results (usually transformation and selection of some kind).
 * </ul>
 *
 * @param <Q> A type of a queried object.
 * @param <R> A type of results.
 * @author AGH AgE Team
 */
public interface IQuery<Q, R> {

    /**
     * Executes the query on a provided target and generates results.
     *
     * @param target An object to perform query on.
     * @return Results of the execution.
     */
    @Nonnull
    R execute(Q target);
}
