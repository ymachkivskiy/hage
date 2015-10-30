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
 * Created: 2011-10-28
 * $Id$
 */

package org.jage.variation.mutation.integer;


import it.unimi.dsi.fastutil.ints.IntList;
import org.jage.variation.mutation.AbstractStochasticMutate;

import java.util.List;


/**
 * Abstract class which efficiently unboxes Integer AbstractStochasticMutate.
 *
 * @author AGH AgE Team
 */
public abstract class IntegerAbstractStochasticMutate extends AbstractStochasticMutate<Integer> {

    @Override
    protected final void doMutate(List<Integer> representation, int index) {
        IntList list = (IntList) representation;
        list.set(index, doMutate(list.getInt(index)));
    }

    /**
     * Perform the actual mutation on a primitive int.
     *
     * @param value The old value
     * @return a mutated value
     */
    protected abstract int doMutate(int value);
}
