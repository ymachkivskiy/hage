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
 * File: StatefulComponentVisitor.java
 * Created: 21-02-2013
 * Author: Daniel
 * $Id$
 */

package org.jage.platform.component.pico.visitor;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentFactory;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.visitors.AbstractPicoVisitor;

import org.jage.platform.component.IStatefulComponent;

/**
 * PicoVisitor which eager instantiates Stateful Components at each level of the hierarchy.
 * 
 * @author AGH AgE Team
 */
public class StatefulComponentInitializer extends AbstractPicoVisitor {

	@Override
	public boolean visitContainer(final PicoContainer pico) {
		pico.getComponents(IStatefulComponent.class);
		return true;
	}

	@Override
	public void visitComponentAdapter(final ComponentAdapter<?> componentAdapter) {
	}

	@Override
	public void visitComponentFactory(final ComponentFactory componentFactory) {
	}

	@Override
	public void visitParameter(final Parameter parameter) {
	}
}
