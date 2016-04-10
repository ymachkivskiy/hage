package org.hage.platform.simulationconfig.load.xml;

import org.hage.util.cmd.CommandLineArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@HageComponent
class ConfigurationFilePathProvider {

    private static final String COMPUTATION_CONFIGURATION = "age.computation.conf";

    @Autowired
    private CommandLineArguments arguments;

    public String getPath() {
        return arguments.getCustomOption(COMPUTATION_CONFIGURATION);
    }

}
