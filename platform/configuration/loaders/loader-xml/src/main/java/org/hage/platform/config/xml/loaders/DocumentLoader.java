package org.hage.platform.config.xml.loaders;


import org.dom4j.Document;
import org.hage.platform.component.definition.ConfigurationException;


public interface DocumentLoader {

    Document loadDocument(String path) throws ConfigurationException;
}
