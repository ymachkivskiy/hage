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
 * Created: 2012-01-30
 * $Id$
 */

package org.hage.emas.reproduction;


import org.hage.emas.agent.IndividualAgent;
import org.hage.evaluation.ISolutionEvaluator;
import org.hage.platform.component.provider.IComponentInstanceProvider;
import org.hage.platform.component.provider.IComponentInstanceProviderAware;
import org.hage.solution.ISolution;
import org.hage.solution.ISolutionFactory;
import org.hage.variation.mutation.IMutateSolution;

import javax.inject.Inject;


/**
 * Default implementation of {@link AsexualReproduction}.
 * <p>
 * The parent produces a gamete - copy of its genotype. It is then mutated and becomes the genotype of the child
 * <p>
 * The parent also gives 1/3 of its energy to the newborn child.
 *
 * @author AGH AgE Team
 */
public class DefaultAsexualReproduction implements AsexualReproduction<IndividualAgent>,
                                                   IComponentInstanceProviderAware {

    private static final double ENERGY_FRACTION = 0.33;

    @Inject
    private ISolutionFactory<ISolution> solutionFactory;

    @Inject
    private IMutateSolution<ISolution> mutate;

    @Inject
    private ISolutionEvaluator<ISolution, Double> evaluator;

    private IComponentInstanceProvider provider;

    @Override
    public IndividualAgent reproduce(final IndividualAgent parent) {
        final ISolution gamete = createGamete(parent);
        final IndividualAgent child = createChild(gamete);
        transferEnergy(parent, child);
        return child;
    }

    private ISolution createGamete(final IndividualAgent parent) {
        final ISolution gamete = solutionFactory.copySolution(parent.getSolution());
        mutate.mutateSolution(gamete);
        return gamete;
    }

    private IndividualAgent createChild(final ISolution gamete) {
        final IndividualAgent child = provider.getInstance(IndividualAgent.class);
        child.setSolution(gamete);
        child.setOriginalFitness(evaluator.evaluate(gamete));
        return child;
    }

    private void transferEnergy(final IndividualAgent parent, final IndividualAgent child) {
        final double parentGift = parent.getEnergy() * ENERGY_FRACTION;

        child.changeEnergyBy(parentGift);
        parent.changeEnergyBy(-parentGift);
    }

    @Override
    public void setInstanceProvider(final IComponentInstanceProvider provider) {
        this.provider = provider;
    }
}
