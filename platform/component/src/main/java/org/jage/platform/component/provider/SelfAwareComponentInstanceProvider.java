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
 * Created: 2012-03-01
 * $Id$
 */

package org.jage.platform.component.provider;


import org.jage.platform.component.definition.IComponentDefinition;

import java.lang.reflect.Type;
import java.util.Collection;


/**
 * Default implementation of {@link ISelfAwareComponentInstanceProvider}, wrapping a {@link IComponentDefinition}, along
 * with a default component definition.
 *
 * @author AGH AgE Team
 */
public class SelfAwareComponentInstanceProvider implements ISelfAwareComponentInstanceProvider {

    private final IComponentInstanceProvider provider;

    private final IComponentDefinition definition;

    public SelfAwareComponentInstanceProvider(final IComponentInstanceProvider provider,
            final IComponentDefinition definition) {
        this.provider = provider;
        this.definition = definition;
    }

    @Override
    public Object getInstance() {
        return getInstance(getName());
    }

    @Override
    public String getName() {
        return definition.getName();
    }

    @Override
    public Object getInstance(final String name) {
        return provider.getInstance(name);
    }

    @Override
    public <T> T getInstance(final Class<T> type) {
        return provider.getInstance(type);
    }

    @Override
    public <T> T getParametrizedInstance(final Class<T> type, final Type[] typeParameters) {
        return provider.getParametrizedInstance(type, typeParameters);
    }

    @Override
    public <T> Collection<T> getInstances(final Class<T> type) {
        return provider.getInstances(type);
    }

    @Override
    public Class<?> getComponentType(final String name) {
        return provider.getComponentType(name);
    }
}
