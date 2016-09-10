package org.hage.platform.simulationconfig.load.xml.loaders;


import org.dom4j.Document;
import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.simulationconfig.load.ConfigurationNotFoundException;


public interface DocumentLoader {

    Document loadDocument(String path) throws ConfigurationException, ConfigurationNotFoundException;
}
