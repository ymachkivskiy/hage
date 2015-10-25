package org.jage.platform.config.loader;


import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.ComputationConfiguration;


public interface IConfigurationLoader<SourceT extends ConfigurationSource> {
    ComputationConfiguration loadConfiguration(SourceT source) throws ConfigurationException;
}
