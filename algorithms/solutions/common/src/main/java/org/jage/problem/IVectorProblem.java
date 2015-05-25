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
 * Created: 2011-10-07
 * $Id$
 */

package org.jage.problem;

/**
 * Interface for vector problems, i.e. problems which have some number of dimensions and each one is bounded.
 *
 * @param <R>
 *            The type of the problem bounds
 *
 * @author AGH AgE Team
 */
public interface IVectorProblem<R> extends IProblem {

	/**
	 * Returns the problem's dimension.
	 *
	 * @return the problem's dimension.
	 */
	public int getDimension();

	/**
	 * Returns the problem's lower bound in a given dimension, indexed from 0. It must be not greater than the
	 * {@link #upperBound(int)} in the same dimension.
	 *
	 * @param atDimension
	 *            the given dimension
	 * @return the problem's lower bound in the given dimension
	 * @throws IllegalArgumentException
	 *             if the given dimension is greater than this problem's one or negative
	 */
	public R lowerBound(int atDimension);

	/**
	 * Returns the problem's upper bound in a given dimension, indexed from 0. It must be not smaller than the
	 * {@link #lowerBound(int)} in the same dimension.
	 *
	 * @param atDimension
	 *            the given dimension
	 * @return the problem's upper bound in the given dimension
	 * @throws IllegalArgumentException
	 *             if the given dimension is greater than this problem's one or negative
	 */
	public R upperBound(int atDimension);
}
