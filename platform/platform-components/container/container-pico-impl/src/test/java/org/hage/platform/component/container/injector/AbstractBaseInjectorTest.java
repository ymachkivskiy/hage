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
 * Created: 2012-03-15
 * $Id$
 */

package org.hage.platform.component.container.injector;


import org.hage.platform.component.container.definition.ComponentDefinition;
import org.picocontainer.Injector;


/**
 * Base class for injector tests.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractBaseInjectorTest {

    protected final ComponentDefinition anyDefinition() {
        return definitionFor(Object.class);
    }

    protected final ComponentDefinition definitionFor(final Class<?> targetClass) {
        return new ComponentDefinition("any", targetClass, false);
    }

    protected final <T> Injector<T> injectorFor(final Class<T> targetClass) {
        return injectorFor(definitionFor(targetClass));
    }

    protected abstract <T> Injector<T> injectorFor(final ComponentDefinition definition);

    protected final <T> Injector<T> injectorFor(final T instance) {
        return injectorFor(definitionFor(instance));
    }

    protected final <T> ComponentDefinition definitionFor(final T instance) {
        return definitionFor(instance.getClass());
    }
}
