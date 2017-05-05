package org.hage.platform.node.container.definition;


import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;



@SuppressWarnings("serial")
public class AbstractComponentDefinitionTest {

    private static final String COMPONENT_NAME = "component name";

    /**
     * Tests the short constructor.
     */
    @Test
    public void testAbstractComponentDefinitionConstructor1() {
        AbstractComponentDefinition definition = new AbstractComponentDefinition(COMPONENT_NAME, String.class, true) {
            // Empty
        };
        assertEquals(COMPONENT_NAME, definition.getName());
        assertEquals(String.class, definition.getType());
        assertTrue(definition.isSingleton());
        assertNotNull(definition.getTypeParameters());
        assertTrue(definition.getTypeParameters().isEmpty());
    }

    /**
     * Tests the long constructor.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractComponentDefinitionConstructor2() {
        List<Type> typeParameters = new ArrayList<Type>();
        typeParameters.add(String.class);
        typeParameters.add(mock(Type.class));
        AbstractComponentDefinition definition = new AbstractComponentDefinition(COMPONENT_NAME, String.class,
                                                                                 typeParameters, true) {
            // Empty
        };
        assertEquals(COMPONENT_NAME, definition.getName());
        assertEquals(String.class, definition.getType());
        assertTrue(definition.isSingleton());
        assertEquals(typeParameters, definition.getTypeParameters());
        definition.getTypeParameters().add(Integer.class); // Should throw
    }

    /**
     * Tests constructor arguments operations.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testConstructorArguments() {
        AbstractComponentDefinition definition = new AbstractComponentDefinition(COMPONENT_NAME, String.class, true) {
            // Empty
        };

        assertTrue(definition.getConstructorArguments().isEmpty());

        IArgumentDefinition singleValueProvider = mock(IArgumentDefinition.class);
        definition.addConstructorArgument(singleValueProvider);

        assertEquals(1, definition.getConstructorArguments().size());
        assertTrue(definition.getConstructorArguments().contains(singleValueProvider));
        definition.getConstructorArguments().remove(singleValueProvider); // Should throw
    }

    /**
     * Tests inner components operations.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testInnerComponentDefinitions() {
        AbstractComponentDefinition definition = new AbstractComponentDefinition(COMPONENT_NAME, String.class, true) {
            // Empty
        };

        assertTrue(definition.getInnerComponentDefinitions().isEmpty());

        IComponentDefinition innerComponentDefinition = mock(IComponentDefinition.class);
        definition.addInnerComponentDefinition(innerComponentDefinition);

        assertEquals(1, definition.getInnerComponentDefinitions().size());
        assertTrue(definition.getInnerComponentDefinitions().contains(innerComponentDefinition));

        assertTrue(definition.removeInnerComponentDefinition(innerComponentDefinition));

        assertTrue(definition.getInnerComponentDefinitions().isEmpty());

        definition.getInnerComponentDefinitions().add(innerComponentDefinition); // Should throw
    }

}
