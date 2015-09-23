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

package org.jage.variation.mutation.integer;


import org.jage.random.INormalizedDoubleRandomGenerator;

import javax.inject.Inject;


/**
 * Simple population mutation strategy, that mutates each solution individually using provided solution mutation
 * strategy.
 *
 * @author AGH AgE Team
 */
public final class FixedRangeMutate extends IntegerAbstractStochasticMutate {

    @Inject
    private INormalizedDoubleRandomGenerator rand;

    private double mutationRange;

    public double getMutationRange() {
        return mutationRange;
    }

    public void setMutationRange(final double mutationRange) {
        this.mutationRange = mutationRange;
    }

    @Override
    protected int doMutate(final int old) {
        // Choose a random value from [ old - mutRange, old + mutRange ]
        return (int) (old + mutationRange * (-1 + rand.nextDouble() * 2.0));
    }
}
