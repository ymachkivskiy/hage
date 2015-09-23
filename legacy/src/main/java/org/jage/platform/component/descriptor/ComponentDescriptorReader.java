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


import org.jage.platform.component.definition.ConfigurationException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;


/**
 * Create a ComponentDescriptor from a class with annotations {@link Inject} and {@link Require}.
 *
 * @author AGH AgE Team
 */
public class ComponentDescriptorReader implements IComponentDescriptorReader {

    @Override
    public IComponentDescriptor readDescritptor(final Object classType) throws ConfigurationException,
                                                                               IllegalArgumentException {

        if(!Class.class.isInstance(classType)) {
            throw new ConfigurationException("Method parameter is not a Class");
        }

        final Class<?> classTypeclass = (Class<?>) classType;
        ComponentDescriptor descriptor = new ComponentDescriptor();
        descriptor.setComponentType(classTypeclass);
        descriptor = addConstructors(descriptor, classTypeclass);
        return descriptor;
    }

    private ComponentDescriptor addConstructors(ComponentDescriptor descriptor, final Class<?> classType) {
        final Constructor<?>[] constructors = classType.getConstructors();
        if(constructors.length == 1) {
            final Constructor<?> constructor = constructors[0];
            descriptor = addConstructor(descriptor, constructor);
        }
        return descriptor;
    }

    private ComponentDescriptor addConstructor(final ComponentDescriptor descriptor, final Constructor<?> constructor) {
        final Class<?>[] constructorParameters = constructor.getParameterTypes();
        final List<Class<?>> constructorParametersList = Arrays.asList(constructorParameters);
        descriptor.addConstructorParametersTypes(constructorParametersList);
        return descriptor;
    }
}
