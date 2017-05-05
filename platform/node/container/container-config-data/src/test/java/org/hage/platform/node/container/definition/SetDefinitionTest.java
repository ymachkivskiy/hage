package org.hage.platform.node.container.definition;


import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class SetDefinitionTest {

    @Test
    public void constructorTest() {
        CollectionDefinition definition = new CollectionDefinition("a set", HashSet.class, false);
        assertEquals("a set", definition.getName());
        assertFalse(definition.isSingleton());
    }

    @Test
    public void innerDefinitionTest() {
        CollectionDefinition definition = new CollectionDefinition("a set", HashSet.class, false);
        CollectionDefinition innerDefinition = new CollectionDefinition("inner set", HashSet.class, true);
        definition.addInnerComponentDefinition(innerDefinition);
        assertEquals(1, definition.getInnerComponentDefinitions().size());
        assertEquals(innerDefinition, definition.getInnerComponentDefinitions().get(0));
    }

}
