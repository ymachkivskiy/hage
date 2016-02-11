package org.hage.platform.config.load.xml;

import org.hage.util.cmd.CommandLineArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class ConfigurationFilePathProvider {

    private static final String COMPUTATION_CONFIGURATION = "age.computation.conf";

    @Autowired
    private CommandLineArguments arguments;

    public String getPath() {
        return arguments.getCustomOption(COMPUTATION_CONFIGURATION);
    }

}
