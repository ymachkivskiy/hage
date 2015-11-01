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
 * Created: 2011-03-15
 * $Id$
 */

package org.hage.examples.properties.xml;


/**
 * The sample component to use with XML-defined properties. It is described in the file ExampleComponent.contract.xml.
 *
 * @author AGH AgE Team
 */
public class ExampleComponent {

    @SuppressWarnings("unused")
    private static final String VERSION = "1.0.0";

    private String name = "";

    private int version = 0;

    private Object holder = null;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Object getHolder() {
        return holder;
    }

    public void setHolder(final Object holder) {
        this.holder = holder;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    /**
     * Prints a summary information about the component on the stdout.
     */
    public void printComponentInfo() {
        System.out.println("My name is " + name);
        System.out.println("My version is " + version);
        System.out.println("I'm holding reference to " + holder);
    }
}
