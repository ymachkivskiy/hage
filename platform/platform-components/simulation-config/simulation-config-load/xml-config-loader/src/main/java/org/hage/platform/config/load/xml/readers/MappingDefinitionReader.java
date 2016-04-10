package org.hage.platform.simulationconfig.load.xml.readers;


import com.google.common.collect.ImmutableMap;
import org.dom4j.Element;
import org.hage.platform.component.container.definition.ConfigurationException;

import java.util.Map;



public class MappingDefinitionReader<T> implements IDefinitionReader<T> {

    private final Map<String, IDefinitionReader<T>> readers;

    /**
     * creates a MappingDefinitionReader.
     *
     * @param readers a map of delegate readers.
     */
    public MappingDefinitionReader(final Map<String, IDefinitionReader<T>> readers) {
        this.readers = ImmutableMap.copyOf(readers);
    }

    @Override
    public T read(final Element element) throws ConfigurationException {
        final String elementName = element.getName();
        final IDefinitionReader<T> reader = readers.get(elementName);
        if(reader == null) {
            throw new ConfigurationException("Invalid element type: " + elementName);
        }
        return reader.read(element);
    }
}
