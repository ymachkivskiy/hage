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

package org.jage.random;

/**
 * This interface introduces additional semantics to the {@link IDoubleRandomGenerator} one. <br />
 * <br />
 *
 * It is designed for generators which value is contained in the range [0, 1), i.e. <code>getLowerDouble()</code> should
 * return 0 and <code>getUpperDouble()</code> should return 1, and <code>nextDouble()</code> is guaranteed to be in the
 * range [0, 1).
 *
 * @author AGH AgE Team
 */
public interface INormalizedDoubleRandomGenerator extends IDoubleRandomGenerator {

	/**
	 * Returns a random double value arbitrarily distributed in the range [0, 1).
	 *
	 * @return A random value.
	 */
	@Override
	public double nextDouble();

	/**
	 * Specifies the lower bound of the values that can be returned by <code>nextDouble()</code>.
	 *
	 * @return 0
	 */
	@Override
	public double getLowerDouble();

	/**
	 * Specifies the upper bound of the values that can be returned by <code>nextDouble()</code>.
	 *
	 * @return 1
	 */
	@Override
	public double getUpperDouble();
}
