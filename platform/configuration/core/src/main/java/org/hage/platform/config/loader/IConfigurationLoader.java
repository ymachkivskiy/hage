package org.hage.platform.config.loader;


import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.config.ComputationConfiguration;


public interface IConfigurationLoader<SourceT extends ConfigurationSource> {
    ComputationConfiguration loadConfiguration(SourceT source) throws ConfigurationException;
}
