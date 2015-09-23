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

package org.jage.monitoring.supplier.stringquery;


import com.google.common.base.Splitter;
import org.jage.monitoring.MonitoringException;

import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;


/**
 * Class parses agent and property string query.
 *
 * @author AGH AgE Team
 */
public class QueryElementsProducer {

    public static final char addressPropertySeparator = '@';

    public static final String addressSeparator = "/";

    public static final char propertySeparator = ',';


    /**
     * @param query String representation of agent and property query.
     * @return <code>QueryElements</code> representation of agent and property query.
     */
    public QueryElements produceQueryElements(String query) {
        Iterable<String> addressPropertySplit = addressPropertySplitter(query);
        Iterator<String> addressPropertyIterator = addressPropertySplit.iterator();
        if(!addressPropertyIterator.hasNext()) {
            throw new MonitoringException("Parameter queryList is invalid, no agent address regex");
        }
        List<String> addressList = addressSplitter(addressPropertyIterator.next());
        if(!addressPropertyIterator.hasNext()) {
            throw new MonitoringException("Parameter queryList is invalid, no property name");
        }
        List<String> propertyNameList = propertySplitter(addressPropertyIterator.next());

        return new QueryElements(addressList, propertyNameList);
    }

    private Iterable<String> addressPropertySplitter(String query) {
        return Splitter.on(addressPropertySeparator).omitEmptyStrings().split(query);
    }

    private List<String> addressSplitter(String addresses) {
        Iterable<String> splitedAddress = Splitter.on(addressSeparator).omitEmptyStrings().split(addresses);
        return newArrayList(splitedAddress);
    }

    private List<String> propertySplitter(String properties) {
        Iterable<String> splitedProperties = Splitter.on(propertySeparator).omitEmptyStrings().split(properties);
        return newArrayList(splitedProperties);
    }
}
