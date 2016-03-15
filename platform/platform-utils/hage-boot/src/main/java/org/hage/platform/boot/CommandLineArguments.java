package org.hage.platform.boot;


import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.io.StringWriter;

// TO BE CHANGED : encapsulate into better class
public class CommandLineArguments {

    private static final String HELP_OPTION = "h";
    private static final String HELP_DESCRIPTION = "print this message";
    private static final String PROPERTIES_PREFIX = "D";
    private static final String PROPERTIES_NAME = "property=value";
    private static final String PROPERTIES_DESCRIPTION = "use value for given property";

    private Options options;
    private CommandLine commandLine;
    private String bootstrapperClassName;

    public void parse(Class<?> programClass, String[] arguments) {
        this.bootstrapperClassName = programClass.getSimpleName();
        initOptions();
        try {
            final CommandLineParser parser = new PosixParser();
            commandLine = parser.parse(options, arguments);
        } catch (final ParseException e) {
            throw new InvalidRuntimeArgumentsException(e);
        }
    }

    public boolean containsOption(final String key) {
        return commandLine.hasOption(key);
    }

    public String getCustomOption(final String key) {
        return commandLine.getOptionProperties(PROPERTIES_PREFIX).getProperty(key);
    }

    public String getUsage() {
        final HelpFormatter formatter = new HelpFormatter();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        formatter.printUsage(printWriter, 100, "java " + bootstrapperClassName, options);
        return stringWriter.toString();
    }

    public boolean hasHelpOption() {
        return containsOption(HELP_OPTION);
    }

    @SuppressWarnings("static-access")
    private void initOptions() {
        this.options = new Options();
        options.addOption(HELP_OPTION, "help", false, HELP_DESCRIPTION);

        // -Dproperty=value
        final Option property = OptionBuilder.withArgName(PROPERTIES_NAME).hasArgs(2).withValueSeparator()
            .withDescription(PROPERTIES_DESCRIPTION).create(PROPERTIES_PREFIX);
        options.addOption(property);
    }
}
