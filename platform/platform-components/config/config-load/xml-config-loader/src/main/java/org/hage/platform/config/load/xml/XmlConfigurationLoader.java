package org.hage.platform.config.load.xml;


import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.load.ConfigurationLoadException;
import org.hage.platform.config.load.ConfigurationLoader;
import org.hage.platform.config.load.ConfigurationNotFoundException;
import org.hage.platform.config.load.definition.InputConfiguration;
import org.hage.platform.config.load.xml.loaders.*;
import org.hage.platform.config.load.xml.readers.DocumentReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public final class XmlConfigurationLoader implements ConfigurationLoader {


    @Autowired
    private ConfigurationConversionService configurationConversionService;
    @Autowired
    private ConfigurationFilePathProvider configurationFilePathProvider;

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
    public InputConfiguration load() throws ConfigurationNotFoundException {
        try {
            return getConfiguration();
        } catch (ConfigurationException e) {
            throw new ConfigurationLoadException(e);
        }
    }

    private InputConfiguration getConfiguration() throws ConfigurationException, ConfigurationNotFoundException {
        String path = configurationFilePathProvider.getPath();
        log.debug("Loading document from '{}'", path);

        Document document = loader.loadDocument(path);
        List<IComponentDefinition> definitions = reader.readDocument(document);

        log.debug("Read {} component definitions.", definitions.size());

        return configurationConversionService.convert(definitions);//TODO temporary
    }
}
