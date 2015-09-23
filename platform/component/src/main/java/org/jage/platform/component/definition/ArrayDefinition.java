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
 * Created: 2011-07-07
 * $Id$
 */

package org.jage.platform.component.definition;


import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newLinkedList;
import static java.util.Collections.unmodifiableList;


/**
 * An array component definition.
 *
 * @author AGH AgE Team
 */
public class ArrayDefinition extends AbstractComponentDefinition {

    private static final long serialVersionUID = 1L;

    private final List<IArgumentDefinition> items = newLinkedList();

    /**
     * Creates a new array definition with a given name and array type.
     *
     * @param name        the name of the component
     * @param type        the array type of the component
     * @param isSingleton whether the component has a singleton scope
     */
    public ArrayDefinition(final String name, final Class<?> type, final boolean isSingleton) {
        super(name, type, isSingleton);
    }

    /**
     * Returns the elements type of this array definition.
     *
     * @return the elements type of this array definition
     */
    public Class<?> getElementsType() {
        return getType().getComponentType();
    }

    /**
     * Adds an item to this array definition.
     *
     * @param item an item
     */
    public void addItem(final IArgumentDefinition item) {
        items.add(checkNotNull(item));
    }

    /**
     * Returns a read only view of this array definition's items.
     *
     * @return this array definition's items
     */
    public List<IArgumentDefinition> getItems() {
        return unmodifiableList(items);
    }
}
