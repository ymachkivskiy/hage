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

package org.hage.evaluation.binary;


import org.hage.solution.ISolution;
import org.hage.strategy.IStrategy;


/**
 * An interface for decoding some types of solutions into others.
 *
 * @param <S> The source representation
 * @param <T> The target representation
 * @author AGH AgE Team
 */
public interface ISolutionDecoder<S extends ISolution, T extends ISolution> extends IStrategy {

    /**
     * Decodes the given solution.
     *
     * @param solution the solution to be transformed
     * @return a decoded representation
     */
    public T decodeSolution(S solution);
}