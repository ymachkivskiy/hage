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

package org.hage.problem.realvalued;


import org.hage.problem.ParallelProblem;


/**
 * This class represents the problem domain for a floating-point coded Rastrigin function. <br />
 * http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/TestGO_files/Page2607.htm <br />
 * Default range: [-50, 50]
 *
 * @author AGH AgE Team
 */
public final class RastriginProblem extends ParallelProblem<Double> {

    private static final double DEFAULT_RANGE = 50;

    /**
     * Creates a RastriginProblem with a default range of [-50, 50].
     *
     * @param dimension The dimension of this problem
     */
    public RastriginProblem(Integer dimension) {
        super(dimension, -DEFAULT_RANGE, DEFAULT_RANGE);
    }
}
