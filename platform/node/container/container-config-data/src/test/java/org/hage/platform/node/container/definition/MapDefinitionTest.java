package org.hage.platform.node.container.definition;


import org.junit.Test;

import java.util.HashMap;
import java.util.TreeMap;

import static org.junit.Assert.*;


public class MapDefinitionTest {

    private static final String MAP_NAME = "map name";

    /**
     * Tests the short constructor.
     */
    @Test
    public void testCollectionDefinitionConstructor1() {
        MapDefinition definition = new MapDefinition(MAP_NAME, true);

        assertEquals(MAP_NAME, definition.getName());
        assertEquals(HashMap.class, definition.getType());
        assertTrue(definition.isSingleton());
        assertEquals(2, definition.getTypeParameters().size());
        assertEquals(Object.class, definition.getElementsKeyType());
        assertEquals(Object.class, definition.getElementsValueType());
    }

    /**
     * Tests the long constructor.
     */
    @Test
    public void testCollectionDefinitionConstructor2() {
        MapDefinition definition = new MapDefinition(MAP_NAME, TreeMap.class, String.class, Integer.class, true);

        assertEquals(MAP_NAME, definition.getName());
        assertEquals(TreeMap.class, definition.getType());
        assertTrue(definition.isSingleton());
        assertEquals(2, definition.getTypeParameters().size());
        assertEquals(String.class, definition.getElementsKeyType());
        assertEquals(Integer.class, definition.getElementsValueType());
    }

    @Test
    public void innerDefinitionTest() {
        MapDefinition definition = new MapDefinition("a map", false);
        MapDefinition innerDefinition = new MapDefinition("inner map", true);
        definition.addInnerComponentDefinition(innerDefinition);

        assertEquals(1, definition.getInnerComponentDefinitions().size());
        assertSame(innerDefinition, definition.getInnerComponentDefinitions().get(0));
    }
}

