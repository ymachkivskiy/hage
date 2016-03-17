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
 * Created: 2009-04-20
 * $Id$
 */

package org.hage.platform.component.container.definition;


import java.util.List;
import java.util.Map;
import java.util.Set;


public class ClassWithProperties {

    private String firstConstructorArgument;
    private int secondConstructorArgument;
    private int a;
    private float b;
    private List<Object> list;
    private Map<Object, Object> map;
    private Set<Object> set;

    public ClassWithProperties() {
    }

    public ClassWithProperties(final String first, final Integer second) {
        firstConstructorArgument = first;
        secondConstructorArgument = second;
    }

    public String getFirstConstructorArgument() {
        return firstConstructorArgument;
    }

    public int getSecondConstructorArgument() {
        return secondConstructorArgument;
    }

    public int getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public List<Object> getList() {
        return list;
    }

    public Map<Object, Object> getMap() {
        return map;
    }

    public Set<Object> getSet() {
        return set;
    }

    public void setA(final int a) {
        this.a = a;
    }

    public void setB(final float b) {
        this.b = b;
    }

    public void setList(final List<Object> list) {
        this.list = list;
    }

    public void setMap(final Map<Object, Object> map) {
        this.map = map;
    }

    public void setSet(final Set<Object> set) {
        this.set = set;
    }
}