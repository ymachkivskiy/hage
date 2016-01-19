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
package org.hage.monitoring.supplier.stringquery;


import org.hage.monitoring.MonitoringException;
import org.hage.monitoring.supplier.extractor.DefaultValueExtractor;
import org.hage.monitoring.supplier.extractor.ValueExtractor;
import org.hage.monitoring.supplier.resultprocessor.AgentStringQueryResultProcessor;
import org.hage.platform.component.agent.IAgent;
import org.hage.platform.component.query.HierarchicalQuery;
import org.hage.platform.component.query.IQuery;
import org.hage.platform.component.query.IValueFilter;
import org.hage.platform.component.workplace.Workplace;
import org.hage.property.InvalidPropertyPathException;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithExpectedSize;
import static org.hage.platform.component.query.ValueFilters.address;
import static org.hage.platform.component.query.ValueFilters.pattern;


/**
 * Based on agent string query, class performs hierarchical agent query.
 *
 * @author AGH AgE Team
 */
public class StringQuery implements IQuery<List<Workplace>, Object> {

    private String stringQuery;
    private ValueExtractor extractor = new DefaultValueExtractor();
    private AgentStringQueryResultProcessor resultProcessor;

    public StringQuery(String stringQuery) {
        this.stringQuery = stringQuery;
    }

    @Override
    public Object execute(List<Workplace> targetList) {
        QueryElementsProducer queryElementsSupplier = new QueryElementsProducer();
        QueryElements queryElements = queryElementsSupplier.produceQueryElements(stringQuery);

        List<IAgent> agentList = newArrayListWithExpectedSize(targetList.size());
        List<Object> resultList = newArrayList();
        // for (Workplace workplace : targetList) {
        // resultList.addAll(getValuesFromQueryElements(queryElements,
        // workplace));
        // }
        for(Workplace workplace : targetList) {
            agentList.add(workplace.getAgent());
        }
        resultList = getValuesFromQueryElements(queryElements, agentList);
        Object processedResult = resultProcessor.processResult(resultList);
        return processedResult;
    }

    private List<Object> getValuesFromQueryElements(QueryElements queryElements, Collection<IAgent> target) {

        List<Object> resultList = newArrayList();

        List<IValueFilter<? super IAgent>> valueFilterList = convertStringsToAddressValueFilterList(queryElements.getAddressList());

        HierarchicalQuery hierarchicalQuery = new HierarchicalQuery();
        Collection<IAgent> agentCollection = hierarchicalQuery.matching(valueFilterList).execute(target);
        if(agentCollection.isEmpty()) {
            throw new MonitoringException("No agent found in path " + stringQuery);
        }
        for(IAgent agent : agentCollection) {
            for(String propertyName : queryElements.getPropertyNameList()) {
                try {
                    resultList.add(extractor.extract(agent, propertyName).get());
                } catch(InvalidPropertyPathException ippe) {
                    throw new MonitoringException("No value found in path " + stringQuery);
                }
            }
        }
        return resultList;
    }

    private List<IValueFilter<? super IAgent>> convertStringsToAddressValueFilterList(List<String> addressRegexList) {
        List<IValueFilter<? super IAgent>> valueFilterList = newArrayList();
        for(String addressRegex : addressRegexList) {
            valueFilterList.add(address(pattern(addressRegex)));
        }
        return valueFilterList;
    }

    public void setExtractor(ValueExtractor extractor) {
        this.extractor = extractor;
    }

    public void setResultProcessor(AgentStringQueryResultProcessor resultProcessor) {
        this.resultProcessor = resultProcessor;
    }
}