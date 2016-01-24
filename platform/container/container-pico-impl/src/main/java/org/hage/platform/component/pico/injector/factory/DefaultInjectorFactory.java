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

package org.hage.platform.component.pico.injector.factory;


import com.google.common.collect.ImmutableMap;
import org.hage.platform.component.definition.*;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.behaviors.Cached;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * This class provides a default implementation of a injector factory.
 *
 * @author AGH AgE Team
 */
public final class DefaultInjectorFactory implements InjectorFactory<IComponentDefinition> {

    private static final Map<Class<?>, InjectorFactory<?>> FACTORIES = ImmutableMap.<Class<?>, InjectorFactory<?>> of(
            ArrayDefinition.class, new ArrayInjectorFactory(),
            CollectionDefinition.class, new CollectionInjectorFactory(),
            MapDefinition.class, new MapInjectorFactory(),
            ComponentDefinition.class, new ComponentInjectorFactory());

    @Override
    public <T> ComponentAdapter<T> createAdapter(final IComponentDefinition definition) {
        final Class<?> definitionClass = definition.getClass();
        checkArgument(FACTORIES.containsKey(definitionClass), "Unknown definition type %s", definitionClass);

        // Warnings can be suppressed, as types are being mapped
        @SuppressWarnings("rawtypes")
        InjectorFactory injectorFactory = FACTORIES.get(definitionClass);
        @SuppressWarnings("unchecked")
        ComponentAdapter<T> adapter = injectorFactory.createAdapter(definition);

        if(definition.isSingleton()) {
            adapter = new Cached<T>(adapter);
        }

        return adapter;
    }
}