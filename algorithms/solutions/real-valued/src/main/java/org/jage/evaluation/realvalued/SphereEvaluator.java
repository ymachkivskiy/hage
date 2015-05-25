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
 * Created: 2011-12-02
 * $Id$
 */

package org.jage.evaluation.realvalued;

import org.jage.evaluation.ISolutionEvaluator;
import org.jage.property.ClassPropertyContainer;
import org.jage.solution.IVectorSolution;

import it.unimi.dsi.fastutil.doubles.DoubleList;

/**
 * This class represents floating-point coded Sphere function. <br />
 * Solution: min=0.0, xi=0, i=1..n <br />
 * http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page1113.htm <br />
 * <br />
 * The original problem is a minimalization one but it is much convenient to maximize the problem function. So the
 * original function is modified g(x)=-f(x)
 *
 * @author AGH AgE Team
 */
public final class SphereEvaluator extends ClassPropertyContainer implements
        ISolutionEvaluator<IVectorSolution<Double>, Double> {

	@Override
	public Double evaluate(IVectorSolution<Double> solution) {
		DoubleList representation = (DoubleList)solution.getRepresentation();

		double sum = 0.0;
		for (int i = 0, n = representation.size(); i < n; i++) {
			double value = representation.getDouble(i);
			sum += value * value;
		}

		return -sum;
	}
}
