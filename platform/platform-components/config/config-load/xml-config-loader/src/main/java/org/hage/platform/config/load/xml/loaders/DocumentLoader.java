package org.hage.platform.config.load.xml.loaders;


import org.dom4j.Document;
import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.config.load.ConfigurationNotFoundException;


public interface DocumentLoader {

    Document loadDocument(String path) throws ConfigurationException, ConfigurationNotFoundException;
}
