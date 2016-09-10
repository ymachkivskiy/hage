package org.hage.platform.component.container.definition;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ArrayDefinitionTest {


    @Test
    public void constructorTest() {
        // when
        ArrayDefinition definition = new ArrayDefinition("an array", Object.class, true);

        // then
        assertEquals("an array", definition.getName());
        assertEquals(Object.class, definition.getType());
        assertTrue(definition.isSingleton());
    }

    @Test
    public void innerDefinitionTest() {
        // given
        ArrayDefinition definition = new ArrayDefinition("an array", Object.class, true);
        ArrayDefinition innerDefinition = new ArrayDefinition("inner", Object.class, false);

        // when
        definition.addInnerComponentDefinition(innerDefinition);

        // then
        assertEquals(1, definition.getInnerComponentDefinitions().size());
        assertEquals(innerDefinition, definition.getInnerComponentDefinitions().get(0));
    }

}
