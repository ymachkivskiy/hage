package org.hage.platform.component.container.definition;


import org.junit.Test;

import static org.junit.Assert.*;


public class ReferenceDefinitionTest {

    private final String expectedName = "target";

    private final ReferenceDefinition definition = new ReferenceDefinition(expectedName);

    @Test
    public void testTargetNameGetter() {
        // when
        String actualName = definition.getTargetName();

        // then
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testEquals() {
        // then
        assertTrue(definition.equals(definition));
        assertFalse(definition.equals(new Object()));
        assertFalse(definition.equals(null));
        assertTrue(definition.equals(new ReferenceDefinition(expectedName)));
        assertFalse(definition.equals(new ReferenceDefinition("not" + expectedName)));
    }

    @Test
    public void testHashCode() {
        // given
        ReferenceDefinition other = new ReferenceDefinition(expectedName);

        // then
        assertEquals(definition.hashCode(), other.hashCode());
    }
}
