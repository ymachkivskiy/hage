package org.hage.platform.simulationconfig.load.xml.readers;


import org.dom4j.Element;
import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.component.container.definition.IArgumentDefinition;
import org.hage.platform.component.container.definition.ReferenceDefinition;

import static org.hage.platform.simulationconfig.load.xml.ConfigAttributes.TARGET;
import static org.hage.platform.simulationconfig.load.xml.ConfigUtils.getRequiredAttribute;


public class ReferenceDefinitionReader implements IDefinitionReader<IArgumentDefinition> {

    @Override
    public ReferenceDefinition read(final Element element) throws ConfigurationException {
        final String targetAttribute = getRequiredAttribute(element, TARGET);
        return new ReferenceDefinition(targetAttribute);
    }
}
