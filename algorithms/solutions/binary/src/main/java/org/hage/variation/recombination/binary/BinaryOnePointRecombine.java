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
 * Created: 2011-12-02
 * $Id$
 */

package org.hage.variation.recombination.binary;


import it.unimi.dsi.fastutil.booleans.BooleanList;
import org.hage.variation.recombination.OnePointRecombine;

import java.util.List;


/**
 * Subclass which efficiently unboxes Booleans for {@link OnePointRecombine}.
 *
 * @author AGH AgE Team
 */
public class BinaryOnePointRecombine extends OnePointRecombine<Boolean> {

    @Override
    protected void swap(List<Boolean> representation1, List<Boolean> representation2, int index) {
        BooleanList list1 = (BooleanList) representation1;
        BooleanList list2 = (BooleanList) representation2;

        boolean element = list1.getBoolean(index);
        list1.set(index, list2.getBoolean(index));
        list2.set(index, element);
    }
}
