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
 * Created: 2010-09-14
 * $Id$
 */

package org.jage.platform.component.definition;


import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newLinkedList;
import static java.util.Collections.unmodifiableList;


/**
 * Abstract skeleton implementation of {@link IComponentDefinition}.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractComponentDefinition implements IComponentDefinition {

    private static final long serialVersionUID = 1L;

    private final String name;

    private final Class<?> type;

    private final boolean isSingleton;

    private final List<IArgumentDefinition> constructorArguments = newLinkedList();

    private final List<IComponentDefinition> innerComponentDefinitions = newLinkedList();

    private List<Type> typeParameters;

    /**
     * Constructs a new component definition for a given type and with a given name and with no generic type parameters.
     *
     * @param name        A name of a component to create.
     * @param type        A type of the component.
     * @param isSingleton Whether the component is in a singleton scope.
     */
    public AbstractComponentDefinition(final String name, final Class<?> type, final boolean isSingleton) {
        this(name, type, Collections.<Type> emptyList(), isSingleton);
    }

    /**
     * Constructs a new component definition for a given type and with a given name.
     *
     * @param name           A name of a component to create.
     * @param type           A type of the component, cannot be null.
     * @param isSingleton    Whether the component is in a singleton scope.
     * @param typeParameters A list of type parameters for generic types, cannot be null.
     */
    public AbstractComponentDefinition(final String name, final Class<?> type, final List<Type> typeParameters,
            final boolean isSingleton) {
        this.name = checkNotNull(name);
        this.type = checkNotNull(type);
        this.typeParameters = checkNotNull(typeParameters);
        this.isSingleton = checkNotNull(isSingleton);
    }

    /**
     * Adds a constructor argument to this component definition.
     *
     * @param argument the constructor argument
     */
    public void addConstructorArgument(IArgumentDefinition argument) {
        constructorArguments.add(checkNotNull(argument));
    }

    /**
     * Adds an inner component definition to this component definition.
     *
     * @param innerDefinition the inner definition
     */
    public void addInnerComponentDefinition(IComponentDefinition innerDefinition) {
        innerComponentDefinitions.add(checkNotNull(innerDefinition));
    }

    /**
     * Removes a given inner definition from this component definition. <br />
     * <br />
     * Can be used by configurators to manipulate component definitions data.
     *
     * @param innerDefinition the inner definition to be removed
     * @return true if the inner definition list contained the specified definition
     */
    public boolean removeInnerComponentDefinition(IComponentDefinition innerDefinition) {
        return innerComponentDefinitions.remove(innerDefinition);
    }

    @Override
    public String toString() {
        return toStringHelper(getClass())
                .add("name", getName())
                .add("type", getType())
                .add("isSingleton", isSingleton())
                .toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public List<Type> getTypeParameters() {
        return unmodifiableList(typeParameters);
    }

    @Override
    public boolean isSingleton() {
        return isSingleton;
    }

    @Override
    public List<IArgumentDefinition> getConstructorArguments() {
        return unmodifiableList(constructorArguments);
    }

    @Override
    public List<IComponentDefinition> getInnerComponentDefinitions() {
        return unmodifiableList(innerComponentDefinitions);
    }
}
