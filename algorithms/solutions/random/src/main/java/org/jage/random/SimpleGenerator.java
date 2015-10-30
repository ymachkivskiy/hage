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

package org.jage.random;


import org.jage.property.ClassPropertyContainer;

import java.util.Random;


/**
 * An default {@link IDoubleRandomGenerator} and {@link IIntRandomGenerator}, that simply wraps their API around Sun's
 * Random implementation.
 *
 * @author AGH AgE Team
 */
public final class SimpleGenerator extends ClassPropertyContainer implements INormalizedDoubleRandomGenerator,
                                                                             IIntRandomGenerator {

    private final Random random;

    /**
     * Creates a SimpleGenerator with a default seed.
     */
    public SimpleGenerator() {
        this(System.currentTimeMillis());
    }

    /**
     * Creates a SimpleGenerator with the given seed.
     *
     * @param seed The seed for this generator
     */
    public SimpleGenerator(final long seed) {
        random = new Random(seed);
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(final int range) {
        return random.nextInt(range);
    }

    @Override
    public int getLowerInt() {
        return Integer.MIN_VALUE;
    }

    @Override
    public int getUpperInt() {
        return Integer.MAX_VALUE;
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public double getLowerDouble() {
        return 0;
    }

    @Override
    public double getUpperDouble() {
        return 1;
    }
}
