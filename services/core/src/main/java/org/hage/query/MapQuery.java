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
 * Created: 2011-09-12
 * $Id$
 */

package org.hage.query;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Query implementation that can be used with subclasses of the {@link Map} class.
 *
 * @param <K> A type of keys in the map.
 * @param <V> A type of values in the map.
 * @author AGH AgE Team
 */
public class MapQuery<K, V> extends MultiElementQuery<Entry<K, V>, Map<K, V>, Map<K, V>> {

    /**
     * Constructs a new MapQuery instance. MapQuery requires the target, the result and the element class being
     * provided. It will use them for creating the result object.
     * <p>
     * This constructor uses a HashMap as a default result object.
     * <p>
     * The execution of an empty query will result in the same object being returned. No operation will be performed.
     */
    public MapQuery() {
        this(HashMap.class);
    }

    /**
     * Constructs a new MapQuery instance. MapQuery requires the target, the result and the element class being
     * provided. It will use them for creating the result object.
     * <p>
     * The execution of an empty query will result in the same object being returned. No operation will be performed.
     *
     * @param resultClass A class of the result, it must be a class that can be instantiated. If it is not, a
     *                    {@link QueryException} will be thrown during query execution.
     */
    public MapQuery(Class<?> resultClass) {
        super(Entry.class, Map.class, resultClass);
    }

    @Override
    protected List<Entry<K, V>> getListFromTarget(Map<K, V> target) {
        return Lists.newArrayList(target.entrySet());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<K, V> convertToResult(List<?> newSelected) {
        Map<K, V> result = Maps.newHashMap();
        for(Entry<K, V> entry : (List<Entry<K, V>>) newSelected) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;

    }

    /**
     * Adds a selector to the query.
     *
     * @param selector A selector to add.
     * @return This query.
     */
    @Override
    public MapQuery<K, V> from(IInitialSelector selector) {
        addInitialSelector(selector);
        return this;
    }

    /**
     * Adds a selector to the query.
     *
     * @param valueSelector A selector to add.
     * @param <T>           A type of the selected value.
     * @return This query.
     */
    @Override
    public <T> MapQuery<K, V> select(IValueSelector<? super Entry<K, V>, T> valueSelector) {
        addValueSelector(valueSelector);
        return this;
    }

    /**
     * Adds a selector to the query.
     *
     * @param fields A fields to select. Fields needs to be accessible by getters in the JavaBean naming convention.
     * @return This query.
     */
    @Override
    public MapQuery<K, V> select(String... fields) {
        for(String field : fields) {
            addValueSelector(ValueSelectors.<Entry<K, V>, Object> field(field));
        }
        return this;
    }

    /**
     * Adds a value filter to the query.
     *
     * @param filter A value filter to add.
     * @return This query.
     */
    @Override
    public MapQuery<K, V> matching(IValueFilter<? super Entry<K, V>> filter) {
        addValueFilter(filter);
        return this;
    }

    /**
     * Adds a function that will process results.
     *
     * @param queryFunction A function to execute on results.
     * @return This query.
     */
    @Override
    public MapQuery<K, V> process(IQueryFunction<? super Map<K, V>> queryFunction) {
        addQueryFunction(queryFunction);
        return this;
    }
}
