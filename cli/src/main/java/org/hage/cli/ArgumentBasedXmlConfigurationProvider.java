package org.hage.cli;

import org.hage.platform.config.loader.ConfigurationSource;
import org.hage.platform.config.provider.BaseComputationConfigurationProvider;
import org.hage.platform.config.xml.FileConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@Component
public class ArgumentBasedXmlConfigurationProvider extends BaseComputationConfigurationProvider {

    private static final String COMPUTATION_CONFIGURATION = "age.computation.conf";

    @Autowired
    private CommandLineArgumentsService argumentsService;

    @Override
    protected Optional<ConfigurationSource> getConfigurationSource() {
        return ofNullable(argumentsService.getCustomOption(COMPUTATION_CONFIGURATION))
            .map(FileConfigurationSource::new);
    }


}
