package org.hage.platform.component.simulationconfig.load.xml;


import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.InputStream;
import java.net.URL;

public class XmlConfigStreamUnmarshaller {

    private static final String SCHEMA_LOCATION = "xsd/hage-config.xsd";

    private final SchemaFactory SCHEMA_FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    public HageConfiguration unmarshalConfiguration(InputStream inputStream) {

        try {

            URL schemaUrl = getClass().getClassLoader().getResource(SCHEMA_LOCATION);
            Schema schema = SCHEMA_FACTORY.newSchema(schemaUrl);

            Unmarshaller jaxbUnmarshaller = unmarshallerForSchema(schema);

            return (HageConfiguration) jaxbUnmarshaller.unmarshal(inputStream);

        } catch (JAXBException e) {
            // TODO: 03.05.17 temporary
            throw new XmlConfigurationUnmarshalException(e);
        } catch (SAXException e) {
            throw new XmlConfigurationUnmarshalException(e);
        }
    }

    private Unmarshaller unmarshallerForSchema(Schema schema) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(HageConfiguration.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        jaxbUnmarshaller.setSchema(schema);
        return jaxbUnmarshaller;
    }
}
