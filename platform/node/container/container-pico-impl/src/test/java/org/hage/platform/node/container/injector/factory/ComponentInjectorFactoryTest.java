package org.hage.platform.node.container.injector.factory;


import org.hage.platform.node.container.definition.ComponentDefinition;
import org.hage.platform.node.container.injector.ComponentInjector;
import org.junit.Test;
import org.picocontainer.ComponentAdapter;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * Tests for ComponentInjectorFactory.
 *
 * @author AGH AgE Team
 */
public class ComponentInjectorFactoryTest {

    private final ComponentInjectorFactory factory = new ComponentInjectorFactory();

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullDefinition() {
        // when
        factory.createAdapter(null);
    }

    @Test
    public void shouldCreateComponentInjectors() {
        // given
        final ComponentDefinition definition = new ComponentDefinition("any", Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(ComponentInjector.class)));
    }
}
