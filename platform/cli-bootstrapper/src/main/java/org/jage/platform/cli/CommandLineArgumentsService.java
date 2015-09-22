package org.jage.platform.cli;

import org.apache.commons.cli.*;
import org.jage.platform.argument.InvalidRuntimeArgumentsException;
import org.jage.platform.argument.RuntimeArgumentsService;

public class CommandLineArgumentsService implements RuntimeArgumentsService {

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

    @Override
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

    @Override
	public String getPlatformOption(final String key) {
		return commandLine.getOptionValue(key);
	}

	@Override
	public boolean containsPlatformOption(final String key) {
		return commandLine.hasOption(key);
	}

	@Override
	public String getCustomOption(final String key) {
		return commandLine.getOptionProperties(PROPERTIES_PREFIX).getProperty(key);
	}

	@Override
	public boolean containsCustomOption(final String key) {
		return commandLine.getOptionProperties(PROPERTIES_PREFIX).containsKey(key);
	}

	@Override
	public void printUsage() {
		final HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java " + bootstrapperClassName, options, true);
	}

    @Override
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
