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
 * Created: 2011-11-03
 * $Id$
 */

package org.hage.random;


import org.hage.strategy.IStrategy;


/**
 * An interface for generating random double values. <br />
 * <br />
 * The contract of this interface is that values returned by <code>nextDouble()</code> must be included between those
 * returned by <code>getLowerDouble()</code> and <code>getUpperDouble()</code>. <br />
 * <br />
 * Apart from that, those values can follow an arbitrary distribution, i.e. does not have to be uniformly distributed.
 *
 * @author AGH AgE Team
 */
public interface IDoubleRandomGenerator extends IStrategy {

    /**
     * Returns a random double value arbitrarily distributed in the range
     * <code>[getLowerDouble(), getUpperDouble()]</code>.
     *
     * @return A random value.
     */
    double nextDouble();

    /**
     * Specifies the lower bound of the values that can be returned by <code>nextDouble()</code>.
     *
     * @return This generator's lower bound.
     */
    double getLowerDouble();

    /**
     * Specifies the upper bound of the values that can be returned by <code>nextDouble()</code>.
     *
     * @return This generator's upper bound.
     */
    double getUpperDouble();

}
