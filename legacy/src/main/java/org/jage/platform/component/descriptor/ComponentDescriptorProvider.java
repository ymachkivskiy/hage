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
 * Created: 2010-09-15
 * $Id$
 */

package org.jage.platform.component.descriptor;


import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.component.exception.ComponentException;

import java.util.HashMap;
import java.util.Map;


/**
 * This class allows one to obtain a descriptor for a class declared in a given definition.
 *
 * @author AGH AgE Team
 */
public class ComponentDescriptorProvider {

    private final Map<Class<?>, IComponentDescriptor> componentDescriptors;

    private final IComponentDescriptorReader reader;

    /**
     * Constructs a new <code>ComponentDescriptorProvider</code>.
     */
    public ComponentDescriptorProvider() {
        this.componentDescriptors = new HashMap<Class<?>, IComponentDescriptor>();
        this.reader = new ComponentDescriptorReader();
    }

    /**
     * Returns a descriptor of the component defined in the given definition.
     * <p>
     * Descriptor is created on the base of the type from the definition.
     *
     * @param definition A definition for which a descriptor should be created.
     * @return A descriptor for the given definition.
     * @throws ComponentException If the descriptor could not be found.
     * @see {@link IComponentDefinition#getType()}
     */
    public IComponentDescriptor getDescriptor(IComponentDefinition definition) throws ComponentException {
        Class<?> type = definition.getType();
        if(!componentDescriptors.containsKey(type)) {
            loadDescriptor(type);
        }
        if(componentDescriptors.containsKey(type)) {
            return componentDescriptors.get(type);
        }
        throw new ComponentException(String.format("Descriptor for %s cannot be found and loaded", type));
    }

    private void loadDescriptor(Class<?> type) {
        if(componentDescriptors.get(type) == null) {
            IComponentDescriptor newDescriptor;
            try {
                newDescriptor = reader.readDescritptor(type);
                componentDescriptors.put(type, newDescriptor);
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            } catch(ConfigurationException e) {
                e.printStackTrace();
            }
        }

    }

}
