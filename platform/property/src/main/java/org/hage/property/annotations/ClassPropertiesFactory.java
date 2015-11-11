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
package org.hage.property.annotations;


import org.hage.property.DuplicatePropertyNameException;
import org.hage.property.IClassPropertiesFactory;
import org.hage.property.InvalidPropertyDefinitionException;
import org.hage.property.InvalidPropertyOperationException;
import org.hage.property.MetaPropertiesSet;
import org.hage.property.MetaProperty;
import org.hage.property.PropertiesSet;
import org.hage.property.PropertyException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public final class ClassPropertiesFactory implements IClassPropertiesFactory {

    private final HashMap<Class<?>, Set<MetaProperty>> classMetaProperties = new HashMap<Class<?>, Set<MetaProperty>>();

    private final IPropertiesReader propertiesReader;

    public ClassPropertiesFactory(IPropertiesReader propertiesReader) {
        this.propertiesReader = propertiesReader;
    }

    @Override
    public PropertiesSet getAllProperties(Object object) throws InvalidPropertyDefinitionException {
        try {
            Set<MetaProperty> metaProperties = loadMetaPropertiesFor(object.getClass());

            PropertiesSet propertiesSet = new PropertiesSet();
            for(MetaProperty metaProperty : metaProperties) {
                propertiesSet.addProperty(metaProperty.createPropertyFor(object));
            }

            return propertiesSet;
        } catch(InvalidPropertyOperationException ex) {
            throw new InvalidPropertyDefinitionException(String.format("Unable to create a property for class %s.",
                                                                       object.getClass()), ex);
        } catch(DuplicatePropertyNameException ex) {
            throw new InvalidPropertyDefinitionException(String.format(
                    "Properties with duplicated name exist in class %s.", object.getClass()), ex);
        } catch(PropertyException e) {
            throw new InvalidPropertyDefinitionException(e);
        }
    }

    @Override
    public MetaPropertiesSet getAllMetaProperties(Object object) throws InvalidPropertyDefinitionException {
        try {
            Set<MetaProperty> metaProperties = loadMetaPropertiesFor(object.getClass());

            MetaPropertiesSet metaPropertiesSet = new MetaPropertiesSet();
            metaPropertiesSet.addAllMetaProperties(metaProperties);

            return metaPropertiesSet;
        } catch(PropertyException e) {
            throw new InvalidPropertyDefinitionException(e);
        }
    }

    private Set<MetaProperty> loadMetaPropertiesFor(Class<?> clazz) throws PropertyException {
        Set<MetaProperty> metaProperties = classMetaProperties.get(clazz);
        if(metaProperties == null) {
            metaProperties = new HashSet<MetaProperty>();
            metaProperties.addAll(propertiesReader.readFieldMetaProperties(clazz));
            metaProperties.addAll(propertiesReader.readGetterSetterMetaProperties(clazz));
            classMetaProperties.put(clazz, metaProperties);
        }

        return metaProperties;
    }
}