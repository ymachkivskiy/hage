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

package org.hage.variation;


import org.hage.population.IPopulation;
import org.hage.solution.ISolution;
import org.hage.variation.mutation.IMutatePopulation;
import org.hage.variation.recombination.IRecombinePopulation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hage.population.Populations.emptyPopulation;


/**
 * Tests for VariationOperator.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class VariationOperatorTest {

    @Mock
    private IRecombinePopulation<ISolution> recombine;

    @Mock
    private IMutatePopulation<ISolution> mutate;

    @InjectMocks
    private VariationOperator<ISolution> operator = new VariationOperator<ISolution>();

    @Test
    public void shouldTransformPopulation() {
        // given
        IPopulation<ISolution, Object> population = emptyPopulation();

        // when
        operator.transformPopulation(population);

        // then
        Mockito.verify(recombine).recombinePopulation(population);
        Mockito.verify(mutate).mutatePopulation(population);
    }
}
