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

package org.hage.platform.component.container.injector.factory;


import com.google.common.collect.ImmutableList;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.hage.platform.component.container.injector.*;
import org.picocontainer.ComponentAdapter;

import java.util.List;


/**
 * Factory for ComponentInjectors.
 *
 * @author AGH AgE Team
 */
final class ComponentInjectorFactory implements InjectorFactory<ComponentDefinition> {

    @Override
    public <T> Injector<T> createAdapter(final ComponentDefinition definition) {
        ComponentAdapter<T> instantiator = new ConstructorInjector<T>(definition);
        List<Injector<T>> injectors = ImmutableList.<Injector<T>> of(
                new AutowiringInjector<T>(definition),
                new PropertiesInjector<T>(definition),
                new ComponentInstanceProviderAwareInjector<T>(definition),
                new StatefulComponentInjector<T>(definition));

        return new ComponentInjector<T>(definition, instantiator, injectors);
    }
}
