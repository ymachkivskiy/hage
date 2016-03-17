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


import org.hage.platform.component.container.definition.MapDefinition;
import org.hage.platform.component.container.injector.Injector;
import org.hage.platform.component.container.injector.MapInjector;


/**
 * Factory for MapInjectors.
 *
 * @author AGH AgE Team
 */
final class MapInjectorFactory implements InjectorFactory<MapDefinition> {

    @Override
    public <T> Injector<T> createAdapter(final MapDefinition definition) {
        return new MapInjector<T>(definition);
    }
}
