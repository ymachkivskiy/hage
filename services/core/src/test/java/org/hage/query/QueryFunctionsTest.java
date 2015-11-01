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
 * Created: 2011-09-19
 * $Id$
 */

package org.hage.query;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Tests for the {@link QueryFunctions} class.
 *
 * @author AGH AgE Team
 */
public class QueryFunctionsTest {

    private IQueryFunction<Collection<Integer>> testedQueryFunction;

    private List<Integer> collection;

    @Before
    public void setUp() {
        collection = new ArrayList<Integer>();
        for(int i = 0; i < 30; i++) {
            collection.add(i);
        }
    }

    /**
     * Tests the {@link org.hage.query.QueryFunctions#max()} method.
     */
    @Test
    public void testMax() {
        testedQueryFunction = QueryFunctions.max();

        Collection<Integer> results = testedQueryFunction.execute(collection);

        assertEquals(Collections.singleton(Collections.max(collection)), results);
    }

    /**
     * Tests the {@link org.hage.query.QueryFunctions#max(java.util.Comparator)} method.
     */
    @Test
    public void testMaxWithComparator() {
        Comparator<Integer> comparator = new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        };
        testedQueryFunction = QueryFunctions.max(comparator);

        Collection<Integer> results = testedQueryFunction.execute(collection);

        assertEquals(Collections.singleton(Collections.max(collection, comparator)), results);
    }

    /**
     * Tests the {@link org.hage.query.QueryFunctions#noOperation()} method.
     */
    @Test
    public void testNoOperation() {
        testedQueryFunction = QueryFunctions.noOperation();

        Collection<Integer> results = testedQueryFunction.execute(collection);

        assertEquals(collection, results);
    }

}
