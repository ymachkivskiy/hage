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

package org.hage.random;


import org.hage.property.ClassPropertyContainer;

import javax.inject.Inject;


/**
 * An implementation of {@link IDoubleSymmetricGenerator} which generates values on the base of a Cauchy distribution.
 *
 * @author AGH AgE Team
 */
public final class CauchyGenerator extends ClassPropertyContainer implements IDoubleSymmetricGenerator {

    public static final double DEFAULT_LOCATION = 0.0;

    public static final double DEFAULT_SCALE = 1.0;

    @Inject
    private INormalizedDoubleRandomGenerator rand;

    @Override
    public double nextDouble() {
        return nextDouble(DEFAULT_LOCATION, DEFAULT_SCALE);
    }

    @Override
    public double nextDouble(final double location, final double scale) {
        final double seed = rand.nextDouble();
        return location + scale * Math.tan(Math.PI * (seed - 0.5));
    }

    @Override
    public double getLowerDouble() {
        return Double.MIN_VALUE;
    }

    @Override
    public double getUpperDouble() {
        return Double.MAX_VALUE;
    }
}
