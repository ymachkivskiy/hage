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

package org.jage.emas.agent;


import com.google.common.collect.ImmutableList;
import org.jage.address.agent.AgentAddress;
import org.jage.address.agent.AgentAddressSupplier;
import org.jage.agent.ActionDrivenAggregate;
import org.jage.agent.ISimpleAgentEnvironment;
import org.jage.platform.component.exception.ComponentException;
import org.jage.property.PropertyField;
import org.jage.solution.ISolution;
import org.jage.solution.ISolutionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.collect.Iterables.filter;
import static java.lang.String.format;


/**
 * Default implementation of {@link IslandAgent}.
 *
 * @author AGH AgE Team
 */
public class DefaultIslandAgent extends ActionDrivenAggregate implements IslandAgent {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(DefaultIslandAgent.class);

    @Inject
    private ISolutionFactory<ISolution> solutionFactory;

    private double avgChildEnergy;

    private double avgChildFitness;

    private IndividualAgent bestChild;

    @PropertyField(propertyName = "bestSolutionEver")
    private ISolution bestSolutionEver;

    @PropertyField(propertyName = "bestFitnessEver")
    private double bestFitnessEver;

    private long agentCounter;

    public DefaultIslandAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public DefaultIslandAgent(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    public ISolution getBestSolutionEver() {
        return bestSolutionEver;
    }

    @Override
    public ISimpleAgentEnvironment getEnvironment() {
        return super.getAgentEnvironment();
    }

    public void updateStatistics() {
        updateChildrenStats();
        updateBestStats();

        if(log.isDebugEnabled()) {
            log.debug(getStatisticsLog());
        }
    }

    private void updateChildrenStats() {
        final List<IndividualAgent> agents = getIndividualAgents();
        double totalChildEnergy = 0.0;
        double totalChildFitness = 0.0;

        for(final IndividualAgent agent : agents) {
            totalChildEnergy += agent.getEnergy();
            totalChildFitness += agent.getOriginalFitness();
        }

        final int size = agents.size();
        avgChildEnergy = totalChildEnergy / size;
        avgChildFitness = totalChildFitness / size;
        agentCounter += size;
    }

    private void updateBestStats() {
        for(final IndividualAgent agent : getIndividualAgents()) {
            if(bestChild == null || agent.getOriginalFitness() > bestChild.getOriginalFitness()) {
                bestChild = agent;
            }
        }

        if(bestChild != null && (bestSolutionEver == null || bestChild.getOriginalFitness() > bestFitnessEver)) {
            bestSolutionEver = solutionFactory.copySolution(bestChild.getSolution());
            bestFitnessEver = bestChild.getOriginalFitness();
        }
    }

    public String getStatisticsLog() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getAddress() + " at step " + (getStep() - 1) + "\n")
                .append("\tCurrent agent count: " + getAgents().size() + "\n")
                .append("\tAverage agent count ever: " + (double) agentCounter / getStep() + "\n")
                .append("\tAverage agent energy: " + getAvgChildEnergy() + "\n")
                .append("\tAverage agent fitness: " + getAvgFitness() + "\n")
                .append("\tCurrent best fitness: " + getBestChild().getOriginalFitness() + "\n")
                .append("\tBest fitness ever: " + getBestFitnessEver() + "\n");
        return builder.toString();
    }

    @Override
    public List<IndividualAgent> getIndividualAgents() {
        return ImmutableList.copyOf(filter(getAgents(), IndividualAgent.class));
    }

    public double getAvgChildEnergy() {
        return avgChildEnergy;
    }

    public double getAvgFitness() {
        return avgChildFitness;
    }

    public IndividualAgent getBestChild() {
        return bestChild;
    }

    public double getBestFitnessEver() {
        return bestFitnessEver;
    }

    @Override
    public boolean finish() throws ComponentException {
        log.info(getResultLog());
        return super.finish();
    }

    private String getResultLog() {
        final StringBuilder builder = new StringBuilder("\n\t---=== Computation finished ===---");
        builder.append(format("\n\tBest solution ever (evaluation = %1$.2f): %2$s", bestFitnessEver, bestSolutionEver));
        return builder.toString();
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("address", getAddress()).toString();
    }
}
