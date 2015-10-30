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
 * Created: 2011-05-06
 * $Id$
 */

package org.jage.genetic.action;


import org.jage.action.IActionContext;
import org.jage.agent.IAgent;
import org.jage.population.IPopulation;
import org.jage.property.Property;
import org.jage.solution.ISolution;
import org.jage.variation.IVariationOperator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.jage.genetic.agent.GeneticActionDrivenAgent.Properties.POPULATION;
import static org.jage.population.Populations.emptyPopulation;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * Tests for {@link VariationActionStrategy}.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class VariationActionStrategyTest {

    @Mock
    private IVariationOperator<ISolution> variation;

    @InjectMocks
    private VariationActionStrategy<ISolution> strategy = new VariationActionStrategy<ISolution>();

    @Test
    public void shouldPreselectPopulation() throws Exception {
        // given
        IPopulation<ISolution, Object> population = emptyPopulation();
        Property populationProperty = mock(Property.class);
        IAgent target = mock(IAgent.class);
        IActionContext context = mock(IActionContext.class);

        given(target.getProperty(POPULATION)).willReturn(populationProperty);
        given(populationProperty.getValue()).willReturn(population);

        // when
        strategy.perform(target, context);

        // then
        verify(variation).transformPopulation(population);
    }
}
