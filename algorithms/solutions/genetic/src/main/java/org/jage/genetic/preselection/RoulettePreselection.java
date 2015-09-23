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
 * Created: 2011-05-05
 * $Id$
 */

package org.jage.genetic.preselection;


import org.jage.genetic.scaling.IScaling;
import org.jage.random.INormalizedDoubleRandomGenerator;
import org.jage.utils.CumulativeDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


/**
 * Preselect strategy. Roulette wheel implementation. Scales solutions evaluations using a provided {@link IScaling}.
 *
 * @author AGH AgE Team
 */
public final class RoulettePreselection extends AbstractPreselection {

    private static final Logger LOG = LoggerFactory.getLogger(RoulettePreselection.class);

    @Inject
    private IScaling scaling;

    @Inject
    private INormalizedDoubleRandomGenerator rand;

    @Override
    protected int[] getPreselectedIndices(final double[] values) {
        final CumulativeDistribution distribution = new CumulativeDistribution(scaling.scale(values));
        final int n = values.length;

        final int[] indices = new int[n];
        for(int i = 0; i < n; i++) {
            indices[i] = distribution.getValueFor(rand.nextDouble());
        }

        LOG.debug("Distribution {} resulted in indices {}", distribution, indices);

        return indices;
    }
}
