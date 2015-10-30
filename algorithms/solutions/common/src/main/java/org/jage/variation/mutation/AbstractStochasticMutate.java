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
 * Created: 2011-10-20
 * $Id$
 */

package org.jage.variation.mutation;


import org.jage.random.IDoubleRandomGenerator;
import org.jage.random.IIntRandomGenerator;
import org.jage.solution.IVectorSolution;
import org.jage.strategy.AbstractStrategy;

import javax.inject.Inject;
import java.util.List;


/**
 * Abstract implementation of {@link IMutateSolution}. Features are not mutated independently. Instead, a random subset
 * of features is first selected (the size of this subset is proportional to the chance to mutate). Then these features
 * are mutated.<br />
 * <br />
 * Concrete subclasses are supposed to provide the actual mutation computation.
 *
 * @param <R> the representation type of the solution to be mutated
 * @author AGH AgE Team
 */
public abstract class AbstractStochasticMutate<R> extends AbstractStrategy implements
                                                                           IMutateSolution<IVectorSolution<R>> {

    private static final double DEFAULT_CHANCE_TO_MUTATE = 0.5;

    @Inject
    private IDoubleRandomGenerator doubleRand;

    @Inject
    private IIntRandomGenerator intRand;

    private double chanceToMutate;

    protected AbstractStochasticMutate() {
        this(DEFAULT_CHANCE_TO_MUTATE);
    }

    protected AbstractStochasticMutate(final double chanceToMutate) {
        this.chanceToMutate = chanceToMutate;
    }

    public void setChanceToMutate(final double chanceToMutate) {
        this.chanceToMutate = chanceToMutate;
    }

    @Override
    public final void mutateSolution(final IVectorSolution<R> solution) {
        final List<R> representation = solution.getRepresentation();
        final int size = representation.size();

        int mutatedBitsCount = (int) (chanceToMutate * size);
        final double chanceForExtraBit = chanceToMutate * size - mutatedBitsCount;
        final int extraBit = (doubleRand.nextDouble() < chanceForExtraBit) ? 1 : 0;
        mutatedBitsCount += extraBit;

        final boolean[] alreadyChecked = new boolean[size];
        for(int i = 0; i < mutatedBitsCount; i++) {
            int k = intRand.nextInt(size);
            while(alreadyChecked[k]) {
                k = intRand.nextInt(size);
            }
            alreadyChecked[k] = true;
            doMutate(representation, k);
        }
    }

    /**
     * Mutate the representation at the given index. <br />
     * <br />
     * This method purpose is to allow efficient unboxing in case of representations of primitives. Subclasses can then
     * cast the given representation in the corresponding fastutil collection.
     *
     * @param representation the representation to be mutated
     * @param index          the index at which mutation should occur
     */
    protected abstract void doMutate(List<R> representation, int index);
}
