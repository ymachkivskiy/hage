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

package org.hage.monitoring.supplier;


import org.hage.monitoring.supplier.resultprocessor.AgentStringQueryResultProcessor;
import org.hage.monitoring.supplier.resultprocessor.DefaultAgentStringQueryResultProcessor;
import org.hage.monitoring.supplier.stringquery.StringQuery;
import org.hage.platform.component.query.IQuery;
import org.hage.platform.component.workplace.Workplace;

import java.util.List;


/**
 * Supplier provides data fetched from agents based on given string query.
 *
 * @author AGH AgE Team
 */
public class AgentStringQuerySupplier extends AgentQuerySupplier {

    public AgentStringQuerySupplier(final String stringQuery) {
        super(buildQueryFrom(stringQuery, new DefaultAgentStringQueryResultProcessor()));
    }

    private static IQuery<List<Workplace>, ?> buildQueryFrom(final String stringQuery, AgentStringQueryResultProcessor resultProcessor) {
        StringQuery result = new StringQuery(stringQuery);
        result.setResultProcessor(resultProcessor);
        return result;
    }


    public AgentStringQuerySupplier(final String stringQuery, AgentStringQueryResultProcessor resultProcessor) {
        super(buildQueryFrom(stringQuery, resultProcessor));
    }

}