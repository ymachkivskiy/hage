/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 2010-06-08
 * $Id$
 */

package org.jage.platform.cli;

import javax.annotation.CheckForNull;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import org.jage.annotation.FieldsAreNonnullByDefault;
import org.jage.platform.argument.InvalidRuntimeArgumentsException;
import org.jage.platform.argument.RuntimeArgumentsService;

/**
 * A default implementation of {@link RuntimeArgumentsService} based on Apache CLI Commons library.
 * <p>
 * Platform properties are defined in POSIX style (i.e. <code>--option value</code>). Custom properties are defined in
 * the Java property style (i.e. <code>-Doption=value</code>).
 * 
 * @author AGH AgE Team
 */
@FieldsAreNonnullByDefault
public class CommandLineArgumentsService implements RuntimeArgumentsService {

	private static final String HELP_OPTION = "h";

	private static final String HELP_DESCRIPTION = "print this message";

	private static final String LIFECYCLE_MANAGER_OPTION = "m";

	private static final String LIFECYCLE_MANAGER_DESCRIPTION = "specifies the lifecycle manager class; type"
	        + " the fully qualified class name";

	private static final String PROPERTIES_PREFIX = "D";

	private static final String PROPERTIES_NAME = "property=value";

	private static final String PROPERTIES_DESCRIPTION = "use value for given property";

	private final Options options = new Options();

	private final CommandLine commandLine;

	private final String bootstrapperClassName;

	/**
	 * Default constructor.
	 * 
	 * @param bootstrapperClassName
	 *            a class name of the boostrapper.
	 * @param args
	 *            runtime arguments
	 * 
	 * @throws InvalidRuntimeArgumentsException
	 *             when arguments cannot be parsed.
	 */
	public CommandLineArgumentsService(final String bootstrapperClassName, final String[] args) {
		this.bootstrapperClassName = bootstrapperClassName;
		initOptions();
		try {
			final CommandLineParser parser = new PosixParser();
			commandLine = parser.parse(options, args);
		} catch (final ParseException e) {
			throw new InvalidRuntimeArgumentsException(e);
		}
	}

	@Override
	@CheckForNull
	public String getPlatformOption(final String key) {
		return commandLine.getOptionValue(key);
	}

	@Override
	public boolean containsPlatformOption(final String key) {
		return commandLine.hasOption(key);
	}

	@Override
	@CheckForNull
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

	/**
	 * Returns the lifecycle manager class name provided by the user.
	 * 
	 * @return the class name.
	 */
	@CheckForNull
	public String getLifecycleManagerClassName() {
		return getPlatformOption(LIFECYCLE_MANAGER_OPTION);
	}

	/**
	 * Checks whether user requested help.
	 * 
	 * @return true if arguments contained the help option.
	 */
	public boolean hasHelpOption() {
		return containsPlatformOption(HELP_OPTION);
	}

	@SuppressWarnings("static-access")
	private void initOptions() {
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
