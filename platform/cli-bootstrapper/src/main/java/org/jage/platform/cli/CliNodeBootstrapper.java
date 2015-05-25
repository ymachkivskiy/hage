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
 * Created: 2012-08-22
 * $Id$
 */

package org.jage.platform.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jage.lifecycle.DefaultLifecycleManager;
import org.jage.platform.argument.InvalidRuntimeArgumentsException;
import org.jage.platform.component.pico.PicoComponentInstanceProviderFactory;
import org.jage.platform.component.provider.IMutableComponentInstanceProvider;
import org.jage.services.core.LifecycleManager;

/**
 * A node bootstrapper is a starting point of the node. It creates initial components and then calls a configured
 * lifecycle manager. This node bootstrapper may be called from the command line.
 * 
 * @author AGH AgE Team
 */
public class CliNodeBootstrapper {

	private static final Class<?> DEFAULT_LIFECYCLE_MANAGER_CLASS = DefaultLifecycleManager.class;

	private static final Logger log = LoggerFactory.getLogger(CliNodeBootstrapper.class);

	//@formatter:off
	private static final String HEADER =  "+-------------------------+\n"
										+ "|   AAA           EEEEEEE |\n"
										+ "|  AAAAA   gggggg EE      |\n"
										+ "| AA   AA gg   gg EEEEE   |\n"
										+ "| AAAAAAA ggggggg EE      |\n"
										+ "| AA   AA      gg EEEEEEE |\n"
										+ "|          ggggg          |\n"
										+ "+-------------------------+\n\n";
	//@formatter:on

	private IMutableComponentInstanceProvider instanceProvider;

	private final CommandLineArgumentsService argumentsService;

	/**
	 * Constructs the cli-based bootstrapper.
	 * 
	 * @param args
	 *            command-line arguments.
	 */
	public CliNodeBootstrapper(final String[] args) {
		argumentsService = new CommandLineArgumentsService(getClass().getName(), args);
	}

	/**
	 * Starting point of the AgE Platform.
	 * 
	 * @param args
	 *            runtime arguments.
	 */
	public static void main(final String[] args) {
		new CliNodeBootstrapper(args).start();
	}

	/**
	 * Starts the platform.
	 */
	public void start() {
		// Print header
		System.out.print(HEADER);

		// When requesting help, we do not want to process anything.
		if (argumentsService.hasHelpOption()) {
			argumentsService.printUsage();
			return;
		}

		log.debug("Starting AgE Node from the command line.");

		// create a component registry
		instanceProvider = PicoComponentInstanceProviderFactory.createInstanceProvider();

		log.debug("Component registry created.");

		try {
			instanceProvider.addComponentInstance(argumentsService);
			initialize();
		} catch (final InvalidRuntimeArgumentsException e) {
			log.error("Invalid runtime arguments.", e);
			argumentsService.printUsage();
		}
		log.debug("Bootstrapping finished.");
	}

	private void initialize() {
		final String lifecycleManagerClassName = argumentsService.getLifecycleManagerClassName();

		final Class<?> lifecycleManagerClass;
		if (lifecycleManagerClassName == null) {
			lifecycleManagerClass = DEFAULT_LIFECYCLE_MANAGER_CLASS;
		} else {
			try {
				lifecycleManagerClass = Class.forName(lifecycleManagerClassName);
			} catch (final ClassNotFoundException e) {
				throw new InvalidRuntimeArgumentsException(String.format(
				        "Undefined lifecycle manager class: %s. Probably there is a mistake in the class name or "
						+ "the class is not in the classpath. Cannot run the node.", lifecycleManagerClassName), e);
			}
		}
		log.debug("We will use {} as a lifecycle manager.", lifecycleManagerClass);
		instanceProvider.addComponent(lifecycleManagerClass);

		log.debug("Starting the lifecycle manager.");
		instanceProvider.getInstance(LifecycleManager.class);
	}
}
