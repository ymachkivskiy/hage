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
 * Created: 2008-10-07
 * $Id$
 */

package org.hage.platform.component.container.definition;


import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.util.Arrays.asList;


/**
 * The definition of a map component.
 *
 * @author AGH AgE Team
 */
public class MapDefinition extends AbstractComponentDefinition {

    private static final long serialVersionUID = 1L;

    private final Map<IArgumentDefinition, IArgumentDefinition> items = newLinkedHashMap();

    /**
     * Creates a new map definition with a given name, default map type, default elements key and value types.
     *
     * @param name        the name of the component
     * @param isSingleton whether the component has a singleton scope
     */
    public MapDefinition(String name, boolean isSingleton) {
        this(name, HashMap.class, isSingleton);
    }

    /**
     * Creates a new map definition with a given name, map type, default elements key and value types.
     *
     * @param name        the name of the component
     * @param type        the map type of the component
     * @param isSingleton whether the component has a singleton scope
     */
    public MapDefinition(String name, @SuppressWarnings("rawtypes") Class<? extends Map> type, boolean isSingleton) {
        this(name, type, Object.class, Object.class, isSingleton);
    }

    /**
     * Creates a new map definition with a given name, map type, elements key and value types.
     *
     * @param name              the name of the component
     * @param type              the map type of the component
     * @param elementsKeyType   the elements key type of the component
     * @param elementsValueType the elements value type of the component
     * @param isSingleton       whether the component has a singleton scope
     */
    public MapDefinition(String name, @SuppressWarnings("rawtypes") Class<? extends Map> type, Type elementsKeyType,
            Type elementsValueType, boolean isSingleton) {
        super(name, type, asList(elementsKeyType, elementsValueType), isSingleton);
    }

    /**
     * Returns the elements key type of this map definition.
     *
     * @return the elements key type of this map definition
     */
    public Type getElementsKeyType() {
        return getTypeParameters().get(0);
    }

    /**
     * Returns the elements value type of this map definition.
     *
     * @return the elements value type of this map definition
     */
    public Type getElementsValueType() {
        return getTypeParameters().get(1);
    }

    /**
     * Adds a key-value arguments pair to this map definition.
     *
     * @param key   the key argument
     * @param value the value argument
     */
    public void addItem(IArgumentDefinition key, IArgumentDefinition value) {
        items.put(checkNotNull(key), checkNotNull(value));
    }

    /**
     * Returns a read only view of this map definition's items.
     *
     * @return this collection definition's items
     */
    public Map<IArgumentDefinition, IArgumentDefinition> getItems() {
        return Collections.unmodifiableMap(items);
    }
}
