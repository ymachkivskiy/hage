package org.hage.platform.component.simulationconfig.load.xml;

import lombok.extern.slf4j.Slf4j;
import org.hage.platform.HageRuntimeException;
import org.hage.platform.component.simulationconfig.load.ConfigurationLoader;
import org.hage.platform.component.simulationconfig.load.ConfigurationNotFoundException;
import org.hage.platform.component.simulationconfig.load.definition.InputConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.InputStream;

@Slf4j
public class XmlConfigurationLoader implements ConfigurationLoader {

    @Autowired
    private ConfigurationTranslator configurationTranslator;
    @Autowired
    private ConfigurationFilePathProvider filePathProvider;

    @Override
    public InputConfiguration load() throws ConfigurationNotFoundException {
        return filePathProvider.getConfigurationFilePath()
                .map(this::loadInputConfiguration)
                .orElse(null);
    }

    private InputConfiguration loadInputConfiguration(String path) {

        log.info("Loading simulation configuration from '%s'", path);

        try(InputStream configFileIS = new FileInputStream(path)) {

            HageConfiguration configuration = new XmlConfigStreamUnmarshaller().unmarshalConfiguration(configFileIS);

            InputConfiguration inputConfiguration = configurationTranslator.translateToInternal(configuration);
            return inputConfiguration;

        } catch (Exception e) {
            log.error("Error during loading configuration file '" + path + "'", e);
            throw new HageRuntimeException("Eror during loading configuration file from '" + path + "'", e);
        }
    }

}
