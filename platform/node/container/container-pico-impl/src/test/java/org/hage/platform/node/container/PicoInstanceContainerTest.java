package org.hage.platform.node.container;


import org.hage.platform.node.container.definition.ComponentDefinition;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


public class PicoInstanceContainerTest {

    @Test
    public void shouldReturnComponentOfGivenType() {
        // given
        final Class<String> componentType = String.class;
        final PicoInstanceContainer provider = new PicoInstanceContainer();
        provider.addComponent(new ComponentDefinition("name", componentType, true));

        // when
        final String component = provider.getComponent(componentType);

        // then
        assertNotNull(component);
    }

    @Test
    public void shouldReturnAllComponentsOfGivenType() {
        // given
        final Class<String> componentType = String.class;
        final PicoInstanceContainer provider = new PicoInstanceContainer();
        provider.addComponent(new ComponentDefinition("name1", componentType, true));
        provider.addComponent(new ComponentDefinition("name2", componentType, true));

        // when
        final List<String> components = provider.getComponents(componentType);

        // then
        assertThat(components.size(), is(2));
    }

//	ComponentDefinition{name=2d00fd11-e85b-4ebf-99c1-7a4180cc6923, type=class ClassWithProperties, isSingleton=true}
//	ComponentDefinition{name=cde397fb-e636-4522-9a2e-6b87e1252b62, type=class ClassWithProperties, isSingleton=true}
//	ComponentDefinition{name=bf1fab54-6686-4711-9b6c-92b3db294dc3, type=class ClassWithProperties, isSingleton=true}

}
