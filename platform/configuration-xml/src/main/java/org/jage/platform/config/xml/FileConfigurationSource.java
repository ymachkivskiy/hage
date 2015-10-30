package org.jage.platform.config.xml;

import org.jage.platform.config.loader.ConfigurationSource;

public class FileConfigurationSource implements ConfigurationSource {
    private final String filePath;

    public FileConfigurationSource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String sourceLocation() {
        return filePath;
    }

}
