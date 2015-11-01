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

package org.hage.emas.agent;


import org.hage.agent.ISimpleAgent;
import org.hage.solution.ISolution;


/**
 * EMAS individual agent interface.
 *
 * @author AGH AgE Team
 */
public interface IndividualAgent extends ISimpleAgent, EmasAgent {

    @Override
    IslandAgent getEnvironment();

    /**
     * Get this agent's current solution.
     */
    ISolution getSolution();

    /**
     * Set this agent's current solution.
     */
    void setSolution(ISolution solution);

    /**
     * Get this agent's current energy.
     */
    double getEnergy();

    /**
     * Change this agent's energy by the given amount. The agent energy will however not drop below 0.
     */
    void changeEnergyBy(double energyChange);

    /**
     * Get the original fitness of this agent, i.e. the one related to its solution.
     */
    double getOriginalFitness();

    /**
     * Set the original fitness of this agent, i.e. the one related to its solution.
     */
    void setOriginalFitness(double fitness);

    /**
     * Get the effective fitness of this agent, i.e. the one related to its solution and environment.
     */
    double getEffectiveFitness();

    /**
     * Set the effective fitness of this agent, i.e. the one related to its solution and environment.
     */
    void setEffectiveFitness(double fitness);
}
