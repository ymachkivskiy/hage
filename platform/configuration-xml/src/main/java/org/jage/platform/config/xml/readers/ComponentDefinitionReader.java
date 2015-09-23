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
 * Created: 2012-01-28
 * $Id$
 */

package org.jage.platform.config.xml.readers;


import org.dom4j.Element;
import org.jage.platform.component.definition.ComponentDefinition;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IArgumentDefinition;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.config.xml.ConfigAttributes;

import static org.jage.platform.config.xml.ConfigAttributes.CLASS;
import static org.jage.platform.config.xml.ConfigAttributes.IS_SINGLETON;
import static org.jage.platform.config.xml.ConfigAttributes.NAME;
import static org.jage.platform.config.xml.ConfigTags.CONSTRUCTOR_ARG;
import static org.jage.platform.config.xml.ConfigTags.PROPERTY;
import static org.jage.platform.config.xml.ConfigUtils.*;


/**
 * Reader for component definitions. Intended to process {@code <component>} tags.
 *
 * @author AGH AgE Team
 */
public class ComponentDefinitionReader extends AbstractDefinitionReader<IComponentDefinition> {

    @Override
    public ComponentDefinition read(final Element element) throws ConfigurationException {
        final String nameAttribute = getRequiredAttribute(element, NAME);
        final String classAttribute = getRequiredAttribute(element, CLASS);
        final String isSingletonAttribute = getRequiredAttribute(element, IS_SINGLETON);

        final ComponentDefinition definition = new ComponentDefinition(nameAttribute,
                                                                       toClass(classAttribute),
                                                                       toBoolean(isSingletonAttribute));

        for(final Element constructorArg : getChildrenIncluding(element, CONSTRUCTOR_ARG)) {
            final IArgumentDefinition argument = getArgumentReader().read(getChild(constructorArg));
            definition.addConstructorArgument(argument);
        }

        for(final Element propertyArg : getChildrenIncluding(element, PROPERTY)) {
            final String propertyNameAttribute = getRequiredAttribute(propertyArg, ConfigAttributes.NAME);
            final IArgumentDefinition argument = getArgumentReader().read(getChild(propertyArg));
            definition.addPropertyArgument(propertyNameAttribute, argument);
        }

        for(final Element innerElement : getChildrenExcluding(element, CONSTRUCTOR_ARG, PROPERTY)) {
            final IComponentDefinition innerDefinition = getInstanceReader().read(innerElement);
            definition.addInnerComponentDefinition(innerDefinition);
        }

        return definition;
    }
}
