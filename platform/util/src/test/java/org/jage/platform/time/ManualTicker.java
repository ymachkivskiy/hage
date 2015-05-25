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
 * Created: 2012-07-20
 * $Id$
 */

package org.jage.platform.time;

import com.google.common.base.Ticker;

/**
 * A ticker that can be manually moved in time.
 * 
 * @author AGH AgE Team
 */
public class ManualTicker extends Ticker {

	private long nextValue;

	@Override
	public long read() {
		return nextValue;
	}

	/**
	 * Increase value that this ticker holds.
	 * 
	 * @param value
	 *            a number to add to the current value.
	 */
	public void increaseValue(final long value) {
		this.nextValue += value;
	}

}
