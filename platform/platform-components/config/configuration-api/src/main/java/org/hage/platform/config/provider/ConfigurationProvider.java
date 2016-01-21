package org.hage.platform.config.provider;


import org.hage.platform.component.definition.ConfigurationException;

public interface ConfigurationProvider<SourceT extends ConfigurationSource> {
    Configuration getConfiguration(SourceT source) throws ConfigurationException;
}
