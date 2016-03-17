package org.hage.platform.config.load.xml.loaders;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.config.load.ConfigurationNotFoundException;
import org.hage.util.io.IncorrectUriException;
import org.hage.util.io.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;


public final class RawDocumentLoader extends AbstractDocumentLoader {

    private static final Logger LOG = LoggerFactory.getLogger(RawDocumentLoader.class);

    private final SAXReader reader;

    public RawDocumentLoader() throws ConfigurationException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);

            SAXParser parser = factory.newSAXParser();
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                               "http://www.w3.org/2001/XMLSchema");
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", getClass().getClassLoader()
                    .getResourceAsStream("age.xsd"));

            reader = new SAXReader(parser.getXMLReader());
            reader.setValidation(true);

        } catch(final ParserConfigurationException e) {
            throw newConfigurationException(e);
        } catch(final SAXException e) {
            throw newConfigurationException(e);
        }
    }

    private ConfigurationException newConfigurationException(final Exception e) throws ConfigurationException {
        return new ConfigurationException("Could not instantiate loader", e);
    }

    @Override
    public Document loadDocument(final String path) throws ConfigurationException, ConfigurationNotFoundException {
        try (InputStream input = ResourceLoader.getResource(path).getInputStream()) {
            reader.setErrorHandler(new AgEXmlErrorHandler(path));
            return reader.read(input);
        } catch(final IncorrectUriException e) {
            throw new ConfigurationNotFoundException("Could not locate resource with path " + path, e);
        } catch(final IOException e) {
            throw new ConfigurationNotFoundException("Could not open resource with path " + path, e);
        } catch(final DocumentException e) {
            throw new ConfigurationException("Could not read resource with path " + path, e);
        }
    }


    /**
     * Custom error handler.
     *
     * @author AGH AgE Team
     */
    private static final class AgEXmlErrorHandler implements ErrorHandler {

        private final String path;

        private AgEXmlErrorHandler(final String path) {
            this.path = path;
        }

        @Override
        public void warning(final SAXParseException e) throws SAXException {
            LOG.warn(createLogString(e));
            throw e;
        }

        @Override
        public void error(final SAXParseException e) throws SAXException {
            LOG.error(createLogString(e));
            throw e;
        }

        @Override
        public void fatalError(final SAXParseException e) throws SAXException {
            LOG.error(createLogString(e));
            throw e;
        }

        private String createLogString(final SAXParseException e) {
            return format("'%s':%s:%s: %s", path, e.getLineNumber(), e.getColumnNumber(), cleanMessage(e.getMessage()));
        }

        private String cleanMessage(final String message) {
            final String[] split = message.split(":");
            final String cleanedMessage = split.length > 1 ? split[1] : split[0];
            return cleanedMessage.trim();
        }
    }
}
