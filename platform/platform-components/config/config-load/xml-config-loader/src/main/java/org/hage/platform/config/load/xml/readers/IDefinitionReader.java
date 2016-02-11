package org.hage.platform.config.load.xml.readers;


import org.dom4j.Element;
import org.hage.platform.component.definition.ConfigurationException;


/**
 * Basic interface for reading configuration definitions from a DOM element.
 *
 * @param <T> the type of definition returned
 * @author AGH AgE Team
 */
public interface IDefinitionReader<T> {

    /**
     * Reads a configuration definition from the given DOM element.
     *
     * @param element the element to read from
     * @return a definition
     * @throws ConfigurationException if the element is invalid or some error happens
     */
    T read(Element element) throws ConfigurationException;
}
