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
 * Created: 2011-05-28
 * $Id$
 */

package org.jage.genetic.scaling;

import org.jage.strategy.AbstractStrategy;

/**
 * Uniformly scales an array of values, so that they are positive, translated to 0 (regarding the smallest element),
 * normalized, and aggregated to 1.
 *
 * @author AGH AgE Team
 */
public final class SimpleScaling extends AbstractStrategy implements IScaling {

	@Override
	public double[] scale(double[] data) {
		if (data.length == 0) {
			throw new IllegalArgumentException("Data can't be empty");
		}

		// Find minimum and sum
		double min = data[0];
		double sum = 0;
		for (double value : data) {
			if (value < min) {
				min = value;
			}
			sum += value;
		}

		// Normalizes the data so that it sums up to 1 and then ... sum it up!

		// Same as substracting from all values and suming up again
		sum -= min * data.length;

		if (sum == 0) {
			// All elements are equal! Special case.
			data[0] = 1.0 / data.length;
			for (int i = 1; i < data.length - 1; i++) {
				data[i] = Math.min(data[i - 1] + 1.0 / data.length, 1.0);
			}
		} else {
			data[0] = (data[0] - min) / sum;
			for (int i = 1; i < data.length - 1; i++) {
				data[i] = Math.min(data[i - 1] + (data[i] - min) / sum, 1.0);
			}
		}
		data[data.length - 1] = 1.0;

		return data;
	}
}
