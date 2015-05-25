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

package org.jage.examples.properties.xml;

/**
 * The sample component to use with XML-defined properties. It is described in the file
 * SimpleFunctionCounter.contract.xml.
 *
 * @author AGH AgE Team
 */
public class SimpleFunctionCounter {

	@SuppressWarnings("unused")
	private final String version = "1.0.0";

	private int x;

	private int y;

	/**
	 * Creates a SimpleFunctionCounter instance.
	 */
	public SimpleFunctionCounter() {
		this(0, 0);
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}

	/**
	 * Creates a SimpleFunctionCounter instance.
	 *
	 * @param x
	 *            X parameter.
	 * @param y
	 *            Y parameter.
	 */
	public SimpleFunctionCounter(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns a result of <code>x^2 + y^2</code> and increases values of x and y.
	 *
	 * @return A result of computation.
	 */
	public int countSquareSum() {
		final int result = x * x + y * y;
		x++;
		y++;
		return result;
	}

	@Override
	public String toString() {
		return x + "^2 + " + y + "^2";
	}
}
