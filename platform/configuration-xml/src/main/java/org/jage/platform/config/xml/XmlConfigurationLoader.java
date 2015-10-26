package org.jage.platform.config.xml;


import org.dom4j.Document;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.component.definition.IComponentDefinition;
import org.jage.platform.config.ComputationConfiguration;
import org.jage.platform.config.ConfigurationConversionService;
import org.jage.platform.config.loader.ConfigurationSource;
import org.jage.platform.config.loader.IConfigurationLoader;
import org.jage.platform.config.xml.loaders.*;
import org.jage.platform.config.xml.readers.DocumentReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public final class XmlConfigurationLoader implements IConfigurationLoader {

    private static final Logger LOG = LoggerFactory.getLogger(XmlConfigurationLoader.class);

    @Autowired
    private ConfigurationConversionService configurationConversionService;

    private final DocumentReader reader;
    private final DocumentLoader loader;

    public XmlConfigurationLoader() throws ConfigurationException {
        this(createLoaderChain(), new DocumentReader());
    }

    XmlConfigurationLoader(final DocumentLoader loader, final DocumentReader reader) {
        this.loader = loader;
        this.reader = reader;
    }

    private static DocumentLoader createLoaderChain() throws ConfigurationException {
        return new RawDocumentLoader()
                .append(new PlaceholderResolver())
                .append(new DocumentResolver())
                .append(new BlockUnwrapper())
                .append(new AnonymousComponentsNamer())
                .append(new ComponentsNormalizer())
                .append(new NestedDefinitionsHandler())
                .append(new ArgumentShortcutExtractor())
                .append(new MultipleTagDuplicator());
    }

    @Override
    public ComputationConfiguration loadConfiguration(ConfigurationSource source) throws ConfigurationException {
        final String path = source.sourceLocation();

        LOG.debug("Loading document from '{}'", path);
        final Document document = loader.loadDocument(path);

        final List<IComponentDefinition> definitions = reader.readDocument(document);
        LOG.debug("Read {} component definitions.", definitions.size());

        return configurationConversionService.convert(definitions);
    }
}
