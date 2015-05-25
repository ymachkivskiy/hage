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
 * File: M7Evaluator.java
 * Created: 29-10-2012
 * Author: Krzywicki
 * $Id$
 */

package org.jage.evaluation.binary;

import org.jage.evaluation.ISolutionEvaluator;
import org.jage.solution.IVectorSolution;

import it.unimi.dsi.fastutil.booleans.BooleanList;

/**
 * A deceptive multimodal function M7.
 *
 * <p>
 * Function M7 has 32 global maxima of value equal to 5, and several million local maxima, the values of which are
 * between 3.203 and 4.641
 *
 * @author AGH AgE Team
 */
public class M7Evaluator implements ISolutionEvaluator<IVectorSolution<Boolean>, Double> {

	@Override
	public Double evaluate(final IVectorSolution<Boolean> solution) {
		final BooleanList representation = (BooleanList)solution.getRepresentation();

		double sum = 0;
		for (int i = 0; i < 5; i++) {
			int count = 0;
			for (int j = 0; j < 6; j++) {
				if (representation.getBoolean(6 * i + j)) {
					count++;
				}
			}
			sum += u(count);
		}

		return sum;
	}

	private double u(final int count) {
		switch (count) {
			case (0):
				return 1;
			case (1):
				return 0;
			case (2):
				return 0.389049;
			case (3):
				return 0.640576;
			case (4):
				return 0.389049;
			case (5):
				return 0;
			case (6):
				return 1;
		}
		throw new IllegalStateException();
	}
}
