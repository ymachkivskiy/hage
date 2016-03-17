package org.hage.platform.config.load.xml;


import org.hage.platform.component.container.definition.ClassWithProperties;
import org.hage.platform.component.container.definition.IComponentDefinition;

import java.util.List;

import static org.hage.platform.component.container.builder.ConfigurationBuilder.Configuration;


public class ConfigurationLoadingFunctionalTest {

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
