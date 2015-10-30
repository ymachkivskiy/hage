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

package org.jage.solution.realvalued;


import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import org.jage.problem.IVectorProblem;
import org.jage.property.ClassPropertyContainer;
import org.jage.random.INormalizedDoubleRandomGenerator;
import org.jage.solution.ISolutionFactory;
import org.jage.solution.IVectorSolution;
import org.jage.solution.VectorSolution;

import javax.inject.Inject;
import java.util.List;


/**
 * Factory for creating IVectorSolution<Double> instances.
 *
 * @author AGH AgE Team
 */
public class RealValuedSolutionFactory extends ClassPropertyContainer implements
                                                                      ISolutionFactory<IVectorSolution<Double>> {

    @Inject
    private INormalizedDoubleRandomGenerator rand;

    @Inject
    private IVectorProblem<Double> problem;

    @Override
    public IVectorSolution<Double> createEmptySolution() {
        final double[] representation = new double[problem.getDimension()];
        return new VectorSolution<Double>(new FastDoubleArrayList(representation));
    }

    @Override
    public IVectorSolution<Double> createInitializedSolution() {
        final double[] representation = new double[problem.getDimension()];
        for(int i = 0; i < problem.getDimension(); i++) {
            representation[i] = problem.lowerBound(i) + rand.nextDouble()
                    * (problem.upperBound(i) - problem.lowerBound(i));
        }

        return new VectorSolution<Double>(new FastDoubleArrayList(representation));
    }

    @Override
    public IVectorSolution<Double> copySolution(final IVectorSolution<Double> solution) {
        final DoubleArrayList representation = (DoubleArrayList) solution.getRepresentation();
        return new VectorSolution<Double>(new FastDoubleArrayList(representation));
    }

    /**
     * Helper class with faster equals and compareTo methods.
     *
     * @author AGH AgE Team
     */
    private static class FastDoubleArrayList extends DoubleArrayList {

        private static final long serialVersionUID = -162525815797087029L;

        public FastDoubleArrayList(final double[] representation) {
            super(representation);
        }

        public FastDoubleArrayList(final DoubleArrayList representation) {
            super(representation);
        }

        @Override
        public boolean equals(final Object o) {
            if(o instanceof DoubleArrayList) {
                return super.equals((DoubleArrayList) o);
            } else {
                return super.equals(o);
            }
        }

        @Override
        public int compareTo(final List<? extends Double> l) {
            if(l instanceof DoubleArrayList) {
                return super.compareTo((DoubleArrayList) l);
            } else {
                return super.compareTo(l);
            }
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
