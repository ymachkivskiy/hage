package org.hage.platform.config.xml;


import org.dom4j.Document;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.ConfigurationConversionService;
import org.hage.platform.config.provider.Configuration;
import org.hage.platform.config.provider.ConfigurationProvider;
import org.hage.platform.config.xml.loaders.*;
import org.hage.platform.config.xml.readers.DocumentReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public final class XmlConfigurationProvider implements ConfigurationProvider<FileConfigurationSource> {

    private static final Logger LOG = LoggerFactory.getLogger(XmlConfigurationProvider.class);

    @Autowired
    private ConfigurationConversionService configurationConversionService;

    private final DocumentReader reader;
    private final DocumentLoader loader;

    public XmlConfigurationProvider() throws ConfigurationException {
        this(createLoaderChain(), new DocumentReader());
    }

    XmlConfigurationProvider(final DocumentLoader loader, final DocumentReader reader) {
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
    public Configuration getConfiguration(FileConfigurationSource source) throws ConfigurationException {
        final String path = source.sourceLocation();

        LOG.debug("Loading document from '{}'", path);
        final Document document = loader.loadDocument(path);

        final List<IComponentDefinition> definitions = reader.readDocument(document);
        LOG.debug("Read {} component definitions.", definitions.size());

        return configurationConversionService.convert(definitions);//TODO temporary
    }
}