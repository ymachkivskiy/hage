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
 * Created: 2012-03-12
 * $Id$
 */

package org.jage.emas.action.individual;


import com.google.common.collect.Iterables;
import org.jage.agent.AgentException;
import org.jage.emas.agent.IndividualAgent;
import org.jage.emas.battle.Battle;
import org.jage.emas.energy.EnergyTransfer;
import org.jage.emas.predicate.IPredicate;
import org.jage.emas.reproduction.AsexualReproduction;
import org.jage.emas.reproduction.SexualReproduction;
import org.jage.emas.util.ChainingAction;
import org.jage.query.AgentEnvironmentQuery;
import org.jage.random.IIntRandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;

import static org.jage.action.AgentActions.addToParent;
import static org.jage.query.ValueFilters.eq;
import static org.jage.query.ValueFilters.not;


/**
 * This action handler performs encounter actions on agents.
 * <p>
 * For the action's agent target, a random neighbor is selected. If both are able to reproduce, according to some
 * predicate, they do. Otherwise, they fight.
 * <p>
 * If the neighborhood is empty, the agent may be able to reproduce itself asexually, based again on some predicate.
 * <p>
 * Reproduction and Battle implementations are delegated to strategies.
 *
 * @author AGH AgE Team
 */
public final class EncounterAction extends ChainingAction<IndividualAgent> {

    private static final Logger log = LoggerFactory.getLogger(EncounterAction.class);

    @Inject
    private IIntRandomGenerator rand;

    @Inject
    private IPredicate<IndividualAgent> reproductionPredicate;

    @Inject
    private SexualReproduction<IndividualAgent> sexualReproductionStrategy;

    @Inject
    private AsexualReproduction<IndividualAgent> asexualReproductionStrategy;

    @Inject
    private Battle<IndividualAgent> battleStrategy;

    @Inject
    private EnergyTransfer<IndividualAgent> battleEnergyTransfer;

    @Override
    public void doPerform(final IndividualAgent agent) throws AgentException {
        log.debug("Performing encounter action on {}", agent);

        final Collection<IndividualAgent> neighborhood = queryForNeighbors(agent);
        if(!neighborhood.isEmpty()) {
            final IndividualAgent other = getRandomElement(neighborhood);
            log.debug("Encounter between agents {} and {}.", agent, other);

            if(reproductionPredicate.apply(agent) && reproductionPredicate.apply(other)) {
                reproductionBetween(agent, other);
            } else {
                battleBetween(agent, other);
            }
        } else if(reproductionPredicate.apply(agent)) {
            selfReproduction(agent);
        }
    }

    private Collection<IndividualAgent> queryForNeighbors(final IndividualAgent agent) throws AgentException {
        return new AgentEnvironmentQuery<IndividualAgent, IndividualAgent>().matching(not(eq(agent))).execute(
                agent.getEnvironment());
    }

    private <T> T getRandomElement(final Collection<T> collection) {
        return Iterables.get(collection, rand.nextInt(collection.size()));
    }

    private void reproductionBetween(final IndividualAgent agent, final IndividualAgent other) throws AgentException {
        final IndividualAgent child = sexualReproductionStrategy.reproduce(agent, other);
        log.debug("Love! Agents {} and {} gave birth to {}.", agent, other, child);
        agent.getEnvironment().submitAction(addToParent(agent, child));
    }

    private void battleBetween(final IndividualAgent agent, final IndividualAgent other) throws AgentException {
        final IndividualAgent winner = battleStrategy.fight(agent, other);
        final IndividualAgent loser = winner != agent ? agent : other;
        final double energyLost = battleEnergyTransfer.transferEnergy(loser, winner);
        log.debug("Fight! Agent {} lost {} energy to {}.", loser, energyLost, winner);
    }

    private void selfReproduction(final IndividualAgent agent) throws AgentException {
        final IndividualAgent child = asexualReproductionStrategy.reproduce(agent);
        log.debug("Love? Agent {} spontaneously gave birth to {}.", agent, child);
        agent.getEnvironment().submitAction(addToParent(agent, child));
    }
}
