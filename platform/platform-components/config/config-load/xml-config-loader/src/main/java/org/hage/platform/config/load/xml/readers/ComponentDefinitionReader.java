package org.hage.platform.config.load.xml.readers;


import org.dom4j.Element;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.component.container.definition.IArgumentDefinition;
import org.hage.platform.component.container.definition.IComponentDefinition;
import org.hage.platform.config.load.xml.ConfigAttributes;

import static org.hage.platform.config.load.xml.ConfigAttributes.*;
import static org.hage.platform.config.load.xml.ConfigTags.CONSTRUCTOR_ARG;
import static org.hage.platform.config.load.xml.ConfigTags.PROPERTY;
import static org.hage.platform.config.load.xml.ConfigUtils.*;


public class ComponentDefinitionReader extends AbstractDefinitionReader<IComponentDefinition> {

    @Override
    public ComponentDefinition read(final Element element) throws ConfigurationException {
        final String nameAttribute = getRequiredAttribute(element, NAME);
        final String classAttribute = getRequiredAttribute(element, CLASS);
        final String isSingletonAttribute = getRequiredAttribute(element, IS_SINGLETON);

        final ComponentDefinition definition = new ComponentDefinition(nameAttribute,
            toClass(classAttribute),
            toBoolean(isSingletonAttribute));

        for (final Element constructorArg : getChildrenIncluding(element, CONSTRUCTOR_ARG)) {
            final IArgumentDefinition argument = getArgumentReader().read(getChild(constructorArg));
            definition.addConstructorArgument(argument);
        }

        for (final Element propertyArg : getChildrenIncluding(element, PROPERTY)) {
            final String propertyNameAttribute = getRequiredAttribute(propertyArg, ConfigAttributes.NAME);
            final IArgumentDefinition argument = getArgumentReader().read(getChild(propertyArg));
            definition.addPropertyArgument(propertyNameAttribute, argument);
        }

        for (final Element innerElement : getChildrenExcluding(element, CONSTRUCTOR_ARG, PROPERTY)) {
            final IComponentDefinition innerDefinition = getInstanceReader().read(innerElement);
            definition.addInnerComponentDefinition(innerDefinition);
        }

        return definition;
    }
}
