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
 * Created: 2012-04-12
 * $Id$
 */

package org.hage.genetic.action;


import org.hage.action.ordering.DefaultActionComparator;


/**
 * This comparator defines a default ordering for genetic actions. The ordering is as follows:
 * <ol>
 * <li>{@link InitializationActionContext}
 * <li>{@link PreselectionActionContext}
 * <li>{@link VariationActionContext}
 * <li>{@link EvaluationActionContext}
 * <li>{@link StatisticsUpdateActionContext}
 * </ol>
 * <p>
 * <p>
 * Additionally, ordering from {@link DefaultActionComparator} is preserved.
 *
 * @author AGH AgE Team
 * @see DefaultActionComparator
 * @since 2.6
 */
public class GeneticActionComparator extends DefaultActionComparator {

    /**
     * Constructs a new comparator instance.
     *
     * @see DefaultActionComparator
     */
    @SuppressWarnings("unchecked")
    public GeneticActionComparator() {
        super();
        addContextsAsOrdered(InitializationActionContext.class, PreselectionActionContext.class,
                             VariationActionContext.class, EvaluationActionContext.class, StatisticsUpdateActionContext.class);
    }
}
