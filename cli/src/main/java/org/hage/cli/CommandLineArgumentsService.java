package org.hage.cli;


import org.apache.commons.cli.*;
import org.springframework.stereotype.Component;

@Component
public class CommandLineArgumentsService {

    private static final String HELP_OPTION = "h";
    private static final String HELP_DESCRIPTION = "print this message";
    private static final String LIFECYCLE_MANAGER_OPTION = "m";
    private static final String LIFECYCLE_MANAGER_DESCRIPTION = "specifies the lifecycle manager class; type"
        + " the fully qualified class name";
    private static final String PROPERTIES_PREFIX = "D";
    private static final String PROPERTIES_NAME = "property=value";
    private static final String PROPERTIES_DESCRIPTION = "use value for given property";


    private Options options;
    private CommandLine commandLine;
    private String bootstrapperClassName;

    public void parse(String bootClassName, String[] arguments) {
        this.bootstrapperClassName = bootClassName;
        initOptions();
        try {
            final CommandLineParser parser = new PosixParser();
            commandLine = parser.parse(options, arguments);
        } catch (final ParseException e) {
            throw new InvalidRuntimeArgumentsException(e);
        }
    }

    public String getPlatformOption(final String key) {
        return commandLine.getOptionValue(key);
    }

    public boolean containsPlatformOption(final String key) {
        return commandLine.hasOption(key);
    }

    public String getCustomOption(final String key) {
        return commandLine.getOptionProperties(PROPERTIES_PREFIX).getProperty(key);
    }

    public boolean containsCustomOption(final String key) {
        return commandLine.getOptionProperties(PROPERTIES_PREFIX).containsKey(key);
    }

    public void printUsage() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java " + bootstrapperClassName, options, true);
    }

    public boolean hasHelpOption() {
        return containsPlatformOption(HELP_OPTION);
    }

    @SuppressWarnings("static-access")
    private void initOptions() {
        this.options = new Options();
        options.addOption(HELP_OPTION, "help", false, HELP_DESCRIPTION);

        final Option lifecycleManagerClass = OptionBuilder.withLongOpt("lifecycle-manager").withArgName("classname")
            .hasArg().withDescription(LIFECYCLE_MANAGER_DESCRIPTION).create(LIFECYCLE_MANAGER_OPTION);
        options.addOption(lifecycleManagerClass);

        // -Dproperty=value
        final Option property = OptionBuilder.withArgName(PROPERTIES_NAME).hasArgs(2).withValueSeparator()
            .withDescription(PROPERTIES_DESCRIPTION).create(PROPERTIES_PREFIX);
        options.addOption(property);
    }
}
