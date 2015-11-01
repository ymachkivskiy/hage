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
 * Created: 2008-10-12
 * $Id$
 */

package org.hage.genetic.agent;


import org.hage.address.agent.AgentAddress;
import org.hage.address.agent.AgentAddressSupplier;
import org.hage.agent.SimpleAgent;
import org.hage.evaluation.ISolutionEvaluator;
import org.hage.genetic.preselection.IPreselection;
import org.hage.population.IPopulation;
import org.hage.property.PropertyField;
import org.hage.solution.ISolution;
import org.hage.solution.ISolutionFactory;
import org.hage.variation.IVariationOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.hage.population.Populations.newPopulation;


/**
 * An agent implementation which performs genetic computation.
 * <p>
 * <br />
 * <br />
 * Deprecated. Use {@link GeneticActionDrivenAgent} instead.
 *
 * @author AGH AgE Team
 */
@Deprecated
public class GeneticAgent extends SimpleAgent {

    private static final long serialVersionUID = 1L;
    private static Logger log = LoggerFactory.getLogger(GeneticAgent.class);

    // BEGIN Dependencies
    /**
     * The number of steps after which statistics will be updated.
     */
    @PropertyField(propertyName = "resolution")
    private final int resolution = 20;
    /**
     * The size of the population.
     */
    @Inject
    @PropertyField(propertyName = "populationSize")
    private int populationSize;
    @Inject
    private ISolutionFactory<ISolution> solutionFactory;
    /**
     * The preselection strategy.
     */
    @Inject
    @PropertyField(propertyName = "preselect")
    private IPreselection<ISolution, Double> preselect;
    /**
     * The variation strategy.
     */
    @Inject
    @PropertyField(propertyName = "geneticOperators")
    private IVariationOperator<ISolution> geneticOperators;

    // END Dependencies

    // BEGIN Properties
    @Inject
    private ISolutionEvaluator<ISolution, Double> evaluator;
    /**
     * The actual step of computation.
     */
    @PropertyField(propertyName = "step")
    private int step = 0;
    /**
     * The population.
     */
    private IPopulation<ISolution, Double> population;

    /**
     * The best solution in the current population.
     */
    @PropertyField(propertyName = "bestSolution")
    private ISolution bestSolution = null;

    /**
     * The average evaluation of the current population.
     */
    @PropertyField(propertyName = "avgEvaluation")
    private double avgEvaluation;

    /**
     * The best solution ever.
     */
    @PropertyField(propertyName = "bestSolutionEver")
    private ISolution bestSolutionEver = null;

    /**
     * The step on which the best solution ever happened.
     */
    @PropertyField(propertyName = "bestSolutionEverStep")
    private int bestSolutionEverStep = 0;

    // END Properties

    // BEGIN Getters

    public GeneticAgent(final AgentAddress address) {
        super(address);
    }

    @Inject
    public GeneticAgent(final AgentAddressSupplier supplier) {
        super(supplier);
    }

    // END Getters

    // BEGIN Logic

    /**
     * Returns the best solution in the current population.
     *
     * @return The best solution
     */
    public ISolution getBestSolution() {
        return bestSolution;
    }

    /**
     * Returns the average evaluation of the current population.
     *
     * @return The average evaluation
     */
    public double getAvgEvaluation() {
        return avgEvaluation;
    }

    @Override
    public void init() {
        createInitialPopulation();
        updatePopulationStatistics();

        log.debug("{}{}", getStepLog(), getPopulationLog("Initial population"));
        log.info("Agent {} initialized", this);
    }

    private void createInitialPopulation() {
        final List<ISolution> solutions = new ArrayList<ISolution>(populationSize);
        final ISolution prototype = solutionFactory.createInitializedSolution();
        for(int i = 0; i < populationSize; i++) {
            solutions.add(solutionFactory.copySolution(prototype));
        }
        population = newPopulation(solutions);
    }

    // END Logic

    // BEGIN Private

    private void updatePopulationStatistics() {
        bestSolution = population.iterator().next();
        double bestEvaluation = evaluator.evaluate(bestSolution);
        avgEvaluation = 0.0;

        for(final ISolution solution : population) {
            final double evaluation = evaluator.evaluate(solution);
            avgEvaluation += evaluation;

            if(evaluation > bestEvaluation) {
                bestSolution = solutionFactory.copySolution(solution);
                bestEvaluation = evaluation;
            }
        }

        avgEvaluation /= population.size();

        if(bestSolutionEver == null) {
            bestSolutionEver = bestSolution;
            bestSolutionEverStep = step;
        } else if(bestEvaluation > evaluator.evaluate(bestSolutionEver)) {
            bestSolutionEver = solutionFactory.copySolution(bestSolution);
            bestSolutionEverStep = step;
        }

        populationSize = population.size();
    }

    private String getStepLog() {
        return this + " at step " + step;
    }

    private String getPopulationLog(final String msg) {
        final StringBuilder builder = new StringBuilder("\n\t---=== " + msg + " ===---");
        for(final ISolution solution : population) {
            builder.append(String.format("\n\t%1$s, Evaluation = %2$g", solution, evaluator.evaluate(solution)));

        }
        return builder.toString();
    }

    @Override
    public void step() {
        step++;

        preselectPopulation();
        if(log.isDebugEnabled()) {
            log.debug("{}{}", getStepLog(), getPopulationLog("Preselected population"));
        }

        transformPopulation();
        if(log.isDebugEnabled()) {
            log.debug("{}{}", getStepLog(), getPopulationLog("Transformed population"));
        }

        if(step % resolution == 0) {
            updatePopulationStatistics();
            log.info("{}{}", getStepLog(), getStatisticsLog());
        }

        notifyMonitorsForChangedProperties();
    }

    // END Private

    // BEGIN Logging

    private void preselectPopulation() {
        population = preselect.preselect(population);
    }

    private void transformPopulation() {
        geneticOperators.transformPopulation(population);
    }

    private String getStatisticsLog() {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.format("\n\tBest solution = %1$s, Evaluation = %2$s, Average evaluation = %3$g",
                                     bestSolution, evaluator.evaluate(bestSolution), avgEvaluation));
        builder.append(String.format("\n\tBest solution ever = %1$s, Evaluation = %2$s, at step = %3$d",
                                     bestSolutionEver, evaluator.evaluate(bestSolutionEver), bestSolutionEverStep));

        return builder.toString();
    }

    // END Logging
}
