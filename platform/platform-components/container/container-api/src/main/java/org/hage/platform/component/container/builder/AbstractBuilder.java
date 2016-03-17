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
package org.hage.platform.component.container.builder;


import org.hage.platform.component.container.definition.*;

import java.util.List;


abstract class AbstractBuilder implements IConfigurationBuilder {

    private final IConfigurationBuilder parent;

    AbstractBuilder(final IConfigurationBuilder parent) {
        this.parent = parent;
    }

    /**
     * Nests the configuration described by the given builder in the current component definition.
     *
     * @param innerConfigurationBuilder The inner builder to be nested
     * @return This builder
     */
    public final IConfigurationBuilder withInner(final IConfigurationBuilder innerConfigurationBuilder) {
        return withInner(innerConfigurationBuilder.build());
    }

    /**
     * Nests the given configuration the current component definition.
     *
     * @param innerConfigurationBuilder The inner builder to be nested
     * @return This builder
     */
    public final IConfigurationBuilder withInner(final List<IComponentDefinition> innerConfiguration) {
        AbstractComponentDefinition currentDefinition = getCurrentDefinition();
        for(IComponentDefinition def : innerConfiguration) {
            currentDefinition.addInnerComponentDefinition(def);
        }
        return this;
    }

    protected abstract AbstractComponentDefinition getCurrentDefinition();

    /**
     * Adds a constructor value argument to the current component definition.
     *
     * @param type        The argument's type
     * @param stringValue The argument's string value.
     * @return This builder
     */
    public AbstractBuilder withConstructorArg(final Class<?> type, final String stringValue) {
        getCurrentDefinition().addConstructorArgument(value(type, stringValue));
        return this;
    }

    protected final IArgumentDefinition value(final Class<?> type, final String stringValue) {
        return new ValueDefinition(type, stringValue);
    }

    // parent IConfigurationBuilder delegation

    /**
     * Adds a constructor reference argument to the current component definition.
     *
     * @param targetName The target component's name.
     * @return This builder
     */
    public AbstractBuilder withConstructorArgRef(final String targetName) {
        getCurrentDefinition().addConstructorArgument(reference(targetName));
        return this;
    }

    protected final IArgumentDefinition reference(final String targetName) {
        return new ReferenceDefinition(targetName);
    }

    @Override
    public final ComponentBuilder Component(final String name, final Class<?> type, final boolean isSingleton) {
        return parent.Component(name, type, isSingleton);
    }

    @Override
    public final ComponentBuilder Component(final String name, final Class<?> type) {
        return parent.Component(name, type);
    }

    @Override
    public final ComponentBuilder Agent(final String name, final Class<?> type) {
        return parent.Agent(name, type);
    }

    @Override
    public final ComponentBuilder Strategy(final String name, final Class<?> type) {
        return parent.Strategy(name, type);
    }

    @Override
    public final CollectionBuilder Set(final String name) {
        return parent.Set(name);
    }

    @Override
    public final CollectionBuilder Set(final String name, final Class<?> type) {
        return parent.Set(name, type);
    }

    @Override
    public final CollectionBuilder Set(final String name, final Class<?> type, final boolean isSingleton) {
        return parent.Set(name, type, isSingleton);
    }

    @Override
    public final CollectionBuilder List(final String name) {
        return parent.List(name);
    }

    @Override
    public final CollectionBuilder List(final String name, final Class<?> type) {
        return parent.List(name, type);
    }

    @Override
    public final CollectionBuilder List(final String name, final Class<?> type, final boolean isSingleton) {
        return parent.List(name, type, isSingleton);
    }

    @Override
    public final ArrayBuilder Array(final String name, final Class<?> type) {
        return parent.Array(name, type);
    }

    @Override
    public final ArrayBuilder Array(final String name, final Class<?> type, final boolean isSingleton) {
        return parent.Array(name, type, isSingleton);
    }

    @Override
    public MapBuilder Map(final String name) {
        return parent.Map(name);
    }

    @Override
    public MapBuilder Map(final String name, final Class<?> keyType, final Class<?> valueType) {
        return parent.Map(name, keyType, valueType);
    }

    // Utility

    @Override
    public MapBuilder Map(final String name, final Class<?> keyType, final Class<?> valueType, final boolean isSingleton) {
        return parent.Map(name, keyType, valueType, isSingleton);
    }

    @Override
    public final List<IComponentDefinition> build() {
        return parent.build();
    }
}
