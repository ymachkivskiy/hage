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
 * Created: 2010-03-08
 * $Id$
 */

package org.jage.platform.component.descriptor;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Default implementation of {@link IComponentDescriptor}.
 *
 * @author AGH AgE Team
 */
public class ComponentDescriptor implements IComponentDescriptor {

    private final List<List<Class<?>>> constructorParametersTypes;
    private Class<?> componentType;

    /**
     * Constructor.
     */
    public ComponentDescriptor() {
        // initialize empty collections
        constructorParametersTypes = new LinkedList<List<Class<?>>>();
    }


    @Override
    public Class<?> getComponentType() {
        return componentType;
    }

    @Override
    public List<List<Class<?>>> getConstructorParametersTypes() {
        return Collections.unmodifiableList(constructorParametersTypes);
    }

    @Override
    public boolean containsProperty(final String name) {
        return false;
    }

    /**
     * Sets component types.
     *
     * @param componentType type of component
     */
    void setComponentType(final Class<?> componentType) {
        if(componentType == null) {
            throw new IllegalArgumentException();
        }
        this.componentType = componentType;
    }

    /**
     * Adds a constructor description by giving a constructor parameters types list.
     *
     * @param parametersTypes list of parameters types in component constructor
     */
    void addConstructorParametersTypes(final List<Class<?>> parametersTypes) {
        constructorParametersTypes.add(parametersTypes);
    }
}
