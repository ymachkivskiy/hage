package org.hage.platform.config.load.xml.readers;


import org.dom4j.Element;
import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.component.container.definition.IArgumentDefinition;
import org.hage.platform.component.container.definition.ValueDefinition;

import static org.hage.platform.config.load.xml.ConfigAttributes.TYPE;
import static org.hage.platform.config.load.xml.ConfigUtils.getRequiredAttribute;
import static org.hage.platform.config.load.xml.ConfigUtils.toClass;


public class ValueDefinitionReader implements IDefinitionReader<IArgumentDefinition> {

    @Override
    public ValueDefinition read(final Element element) throws ConfigurationException {
        final String type = getRequiredAttribute(element, TYPE);
        final String value = element.getText();
        return new ValueDefinition(toClass(type), value);
    }
}
