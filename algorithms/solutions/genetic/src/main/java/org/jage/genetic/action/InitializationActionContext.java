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
 * Created: 2011-12-16
 * $Id$
 */

package org.jage.genetic.action;

import org.jage.action.IActionContext;
import org.jage.action.context.AgentActionContext;

/**
 * This context defines a initialization action. To use it, you should declare a handler bean named 'initializationAction'
 * in the workplace context.
 *
 * @author AGH AgE Team
 */
@AgentActionContext(InitializationActionContext.Properties.INITIALIZATION_ACTION)
public final class InitializationActionContext implements IActionContext {
	/**
	 * InitializationActionContext properties.
	 *
	 * @author AGH AgE Team
	 */
	public static class Properties {
		/**
		 * The action name.
		 */
		public static final String INITIALIZATION_ACTION = "initializationAction";
	}
}
