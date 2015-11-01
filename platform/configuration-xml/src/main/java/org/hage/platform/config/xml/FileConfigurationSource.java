package org.hage.platform.config.xml;

import org.hage.platform.config.loader.ConfigurationSource;

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
