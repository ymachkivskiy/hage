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
package org.hage.solution.realvalued;


import it.unimi.dsi.fastutil.doubles.DoubleList;
import org.hage.solution.ISolutionMetric;
import org.hage.solution.IVectorSolution;
import org.hage.strategy.AbstractStrategy;


public class EuclideanMetric<S extends IVectorSolution<Double>> extends AbstractStrategy implements ISolutionMetric<S> {

    @Override
    public double getDistance(final S solution1, final S solution2) {
        double[] representation1 = ((DoubleList) solution1.getRepresentation()).toDoubleArray();
        double[] representation2 = ((DoubleList) solution2.getRepresentation()).toDoubleArray();

        double sum = 0;
        for(int i = 0; i < representation1.length; i++) {
            double dif = representation1[i] - representation2[i];
            sum += dif * dif;
        }
        return Math.sqrt(sum);
    }
}
