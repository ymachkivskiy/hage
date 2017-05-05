package org.hage.platform.node.container.definition;


import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



public class ListDefinitionTest {

    /**
     * Tests the constructor correctness.
     */
    @Test
    public void constructorTest() {
        CollectionDefinition definition = new CollectionDefinition("a list", LinkedList.class, true);
        assertEquals("a list", definition.getName());
        assertTrue(definition.isSingleton());
        assertEquals(Object.class, definition.getElementsType());
    }

    @Test
    public void innerDefinitionTest() {
        CollectionDefinition definition = new CollectionDefinition("a list", LinkedList.class, true);
        CollectionDefinition innerDefinition = new CollectionDefinition("inner", LinkedList.class, false);
        definition.addInnerComponentDefinition(innerDefinition);
        assertEquals(1, definition.getInnerComponentDefinitions().size());
        assertEquals(innerDefinition, definition.getInnerComponentDefinitions().get(0));
    }

}
