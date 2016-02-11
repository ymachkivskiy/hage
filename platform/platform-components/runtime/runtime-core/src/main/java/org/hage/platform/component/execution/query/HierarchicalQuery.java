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

package org.hage.platform.component.execution.query;


import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.hage.platform.component.execution.agent.IAgent;
import org.hage.platform.component.execution.agent.IAggregate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Sets.newHashSet;


/**
 * @author AGH AgE Team
 */
public class HierarchicalQuery implements IQuery<Collection<IAgent>, Collection<IAgent>> {

    List<IValueFilter<? super IAgent>> valueFilterList;

    public HierarchicalQuery() {
        valueFilterList = new ArrayList<IValueFilter<? super IAgent>>();
    }

    public HierarchicalQuery(IValueFilter<? super IAgent>... valueFilters) {
        valueFilterList = new ArrayList<IValueFilter<? super IAgent>>();
        for(IValueFilter<? super IAgent> iValueFilter : valueFilters) {
            valueFilterList.add(iValueFilter);
        }
    }

    public HierarchicalQuery(List<IValueFilter<? super IAgent>> valueFilterList) {
        this.valueFilterList = valueFilterList;
    }

    public HierarchicalQuery matching(IValueFilter<? super IAgent> valueFfilter) {
        valueFilterList.add(valueFfilter);
        return this;
    }

    public HierarchicalQuery matching(List<IValueFilter<? super IAgent>> valueFilterList) {
        this.valueFilterList = valueFilterList;
        return this;
    }

    @Override
    public Collection<IAgent> execute(Collection<IAgent> target) {
        return hierarchicalQueryAgentsByValueFilter(target, valueFilterList);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Collection hierarchicalQueryAgentsByValueFilter(Collection collection, List<IValueFilter<? super IAgent>> filters) {
        Collection result = newHashSet();

        if(!filters.isEmpty()) {
            IValueFilter head = filters.get(0);
            List<IValueFilter<? super IAgent>> tail = filters.subList(1, filters.size());

            Collection matchingElements = Sets.newHashSet();

            for(Object o : collection) {
                if(head.matches(o)) {
                    matchingElements.add(o);
                }
            }

            if(tail.isEmpty()) {
                result = matchingElements;
            } else {
                for(IAggregate c : Iterables.filter(matchingElements, IAggregate.class)) {
                    result.addAll(hierarchicalQueryAgentsByValueFilter(c, tail));
                }
            }
        }

        return result;
    }

    // @SuppressWarnings({ "rawtypes", "unchecked" })
    // private Collection hierarchicalQueryAgentsByValueFilter(Collection
    // collection, List<IValueFilter<? super IAgent>> filters) {
    // Collection result = newHashSet();
    //
    // if(!filters.isEmpty()) {
    // IValueFilter head = filters.get(0);
    // List<IValueFilter<? super IAgent>> tail = filters.subList(1,
    // filters.size());
    // Collection matchingElements = Sets.newHashSet();
    // for(Object o : collection) {
    // if(head.matches(o)) {
    // matchingElements.add(o);
    // }
    // }
    //
    // if(tail.isEmpty()) {
    // result = matchingElements;
    // } else {
    // for(Collection c : Iterables.filter(matchingElements, Collection.class))
    // {
    // result.add(hierarchicalQueryAgentsByValueFilter(c, tail));
    // }
    // }
    // }
    //
    // return result;
    // }

}
