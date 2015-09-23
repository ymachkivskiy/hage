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
 * Created: 2011-04-07
 * $Id$
 */

package org.jage.examples.delegation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Strategy for adding a "Hello" to a text and printing it.
 *
 * @author AGH AgE Team
 */
public class EchoHelloStrategy implements IEchoStrategy {

    private static final Logger log = LoggerFactory.getLogger(EchoHelloStrategy.class);

    @Override
    public void echo(final String text) {
        log.info("Hello. " + text);
    }
}
