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

package org.jage.evaluation.binary;

import javax.inject.Inject;

import org.jage.evaluation.ISolutionEvaluator;
import org.jage.property.ClassPropertyContainer;
import org.jage.solution.ISolution;

/**
 * An evaluator implementation which applies a {@link ISolutionDecoder} before delegating to some other
 * {@link ISolutionEvaluator}, effectively acting as a bridge.
 *
 * @param <S>
 *            The decoder source solution type
 * @param <T>
 *            The decoder target solution type
 * @author AGH AgE Team
 */
public final class DecodingEvaluator<S extends ISolution, T extends ISolution> extends ClassPropertyContainer implements
        ISolutionEvaluator<S, Double> {

	@Inject
	private ISolutionDecoder<S, T> solutionDecoder;

	@Inject
	private ISolutionEvaluator<T, Double> solutionEvaluator;

	@Override
	public Double evaluate(final S solution) {
		final T decodedSolution = solutionDecoder.decodeSolution(solution);
		return solutionEvaluator.evaluate(decodedSolution);
	}
}
