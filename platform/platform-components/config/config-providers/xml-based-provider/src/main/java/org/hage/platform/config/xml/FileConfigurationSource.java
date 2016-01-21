package org.hage.platform.config.xml;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hage.platform.config.provider.ConfigurationSource;

@ToString
@RequiredArgsConstructor
public class FileConfigurationSource implements ConfigurationSource {
    private final String filePath;

    @Override
    public String sourceLocation() {
        return filePath;
    }

}
