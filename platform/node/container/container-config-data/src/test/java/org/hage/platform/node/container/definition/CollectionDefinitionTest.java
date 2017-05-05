package org.hage.platform.node.container.definition;


import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CollectionDefinitionTest {

    private static final String COLLECTION_NAME = "collection name";

    /**
     * Tests the short constructor.
     */
    @Test
    public void testCollectionDefinitionConstructor1() {
        CollectionDefinition definition = new CollectionDefinition(COLLECTION_NAME, LinkedList.class, true);

        assertEquals(COLLECTION_NAME, definition.getName());
        assertEquals(LinkedList.class, definition.getType());
        assertTrue(definition.isSingleton());
        assertEquals(1, definition.getTypeParameters().size());
        assertEquals(Object.class, definition.getElementsType());
    }

    /**
     * Tests the long constructor.
     */
    @Test
    public void testCollectionDefinitionConstructor2() {
        CollectionDefinition definition = new CollectionDefinition(COLLECTION_NAME, LinkedList.class, String.class,
            true);

        assertEquals(COLLECTION_NAME, definition.getName());
        assertEquals(LinkedList.class, definition.getType());
        assertTrue(definition.isSingleton());
        assertEquals(1, definition.getTypeParameters().size());
        assertEquals(String.class, definition.getElementsType());
    }

}
