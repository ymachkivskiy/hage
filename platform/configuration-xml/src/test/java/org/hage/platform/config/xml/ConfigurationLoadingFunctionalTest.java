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
 * Created: 2012-01-28
 * $Id$
 */

package org.hage.platform.config.xml;


import org.hage.platform.component.definition.ClassWithProperties;
import org.hage.platform.component.definition.IComponentDefinition;

import java.util.List;

import static org.hage.platform.component.builder.ConfigurationBuilder.Configuration;


/**
 * Functional tests for ConfigurationLoader.
 *
 * @author AGH AgE Team
 */
public class ConfigurationLoadingFunctionalTest {

//    @Test
//    public void performFullFunctionalTest() throws ConfigurationException {
//        // given
//        final String source = "classpath:full.xml";
//        System.setProperty(PlaceholderResolver.AGE_PROPERTIES_INCLUDE, "classpath:full.properties");
//        final XmlConfigurationLoader loader = new XmlConfigurationLoader();
//        final List<IComponentDefinition> expected = createExpectedList();
//
//        // when
//        final List<IComponentDefinition> configuration = loader.loadConfiguration(source);
//
//        // then
//        ConfigurationAssert.assertObjectDefinitionListsEqual(expected, configuration);
//    }

    private List<IComponentDefinition> createExpectedList() {
        return Configuration()
                .Component("outerComponent", ClassWithProperties.class, true)
                .withConstructorArg(String.class, "ABC")
                .withConstructorArg(Integer.class, "123")
                .withProperty("a", Integer.class, "4")
                .withProperty("b", Float.class, "3.14")
                .withPropertyRef("list", "list")
                .withPropertyRef("map", "map")
                .withPropertyRef("set", "set")
                .withPropertyRef("objectArray", "objectArray")
                .withPropertyRef("longArray", "longArray")
                .withInner(Configuration()
                                   .Component("innerComponent", ClassWithProperties.class, true)
                                   .Component("innerAgent", ClassWithProperties.class, false)
                                   .Component("innerStrategy", ClassWithProperties.class, true)
                                   .List("list", Object.class, true)
                                   .withItemRef("innerComponent")
                                   .withItemRef("innerAgent")
                                   .withItemRef("multipleComponent")
                                   .withItemRef("multipleComponent")
                                   .withItemRef("multipleComponent")
                                   .withInner(Configuration().Component("multipleComponent", Object.class))
                                   .Map("map", Object.class, Object.class, true)
                                   .withItem()
                                   .key(String.class, "2")
                                   .valueRef("innerComponent")
                                   .withItem()
                                   .key(String.class, "1")
                                   .valueRef("innerAgent")
                                   .Set("set", Object.class, true)
                                   .withItemRef("innerComponent")
                                   .withItem(Integer.class, "2")
                                   .Array("objectArray", Object.class, true)
                                   .withItemRef("innerComponent")
                                   .withItem(Integer.class, "2")
                                   .Array("longArray", Long.class, true)
                                   .withItem(Long.class, "2")
                                   .withItem(Long.class, "4")
                                   .withItem(Long.class, "8"))
                .List("outer1List")
                .Component("outer2Component", Object.class)
                .List("outer2List")
                .List("outer3List")
                .Component("outer4Component", Object.class)
                .withInner(Configuration()
                                   .List("inner4List")
                )
                .List("outer4List")
                .build();
    }
}
