package org.hage.platform.config.loader;


import org.hage.platform.component.definition.ConfigurationException;

public interface IConfigurationLoader<SourceT extends ConfigurationSource> {
    Configuration loadConfiguration(SourceT source) throws ConfigurationException;
}