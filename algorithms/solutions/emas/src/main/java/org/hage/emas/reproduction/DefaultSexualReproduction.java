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
 * Created: 2012-03-16
 * $Id$
 */

package org.hage.emas.reproduction;


import org.hage.emas.agent.IndividualAgent;
import org.hage.evaluation.ISolutionEvaluator;
import org.hage.platform.component.provider.IComponentInstanceProvider;
import org.hage.platform.component.provider.IComponentInstanceProviderAware;
import org.hage.random.INormalizedDoubleRandomGenerator;
import org.hage.solution.ISolution;
import org.hage.solution.ISolutionFactory;
import org.hage.variation.mutation.IMutateSolution;
import org.hage.variation.recombination.IRecombine;

import javax.inject.Inject;


/**
 * Default implementation of {@link SexualReproduction}.
 * <p>
 * Each parent produces a gamete - a copy of its genotype. These are recombined and mutated, and one of them is randomly
 * choosen as the new agent's genotype.
 * <p>
 * Each of the parent also give 1/4 of its energy to the newborn child.
 *
 * @author AGH AgE Team
 */
public class DefaultSexualReproduction implements SexualReproduction<IndividualAgent>, IComponentInstanceProviderAware {

    private static final double ENERGY_FRACTION = 0.25;

    @Inject
    private ISolutionFactory<ISolution> solutionFactory;

    @Inject
    private IRecombine<ISolution> recombination;

    @Inject
    private IMutateSolution<ISolution> mutation;

    @Inject
    private INormalizedDoubleRandomGenerator rand;

    @Inject
    private ISolutionEvaluator<ISolution, Double> evaluator;

    private IComponentInstanceProvider provider;

    @Override
    public IndividualAgent reproduce(final IndividualAgent firstParent, final IndividualAgent secondParent) {
        final ISolution gamete = createGamete(firstParent, secondParent);
        final IndividualAgent child = createChild(gamete);
        transferEnergy(firstParent, secondParent, child);
        return child;
    }

    private ISolution createGamete(final IndividualAgent first, final IndividualAgent second) {
        final ISolution firstGamete = solutionFactory.copySolution(first.getSolution());
        final ISolution secondGamete = solutionFactory.copySolution(second.getSolution());
        recombination.recombine(firstGamete, secondGamete);

        // choose one at random
        final ISolution gamete = rand.nextDouble() <= 0.5 ? firstGamete : secondGamete;
        mutation.mutateSolution(gamete);

        return gamete;
    }

    private IndividualAgent createChild(final ISolution gamete) {
        final IndividualAgent child = provider.getInstance(IndividualAgent.class);
        child.setSolution(gamete);
        child.setOriginalFitness(evaluator.evaluate(gamete));
        return child;
    }

    private void transferEnergy(final IndividualAgent firstParent, final IndividualAgent secondParent,
            final IndividualAgent child) {
        final double firstParentGift = firstParent.getEnergy() * ENERGY_FRACTION;
        final double secondParentGift = secondParent.getEnergy() * ENERGY_FRACTION;

        child.changeEnergyBy(firstParentGift + secondParentGift);
        firstParent.changeEnergyBy(-firstParentGift);
        secondParent.changeEnergyBy(-secondParentGift);
    }

    @Override
    public void setInstanceProvider(final IComponentInstanceProvider provider) {
        this.provider = provider;
    }
}
