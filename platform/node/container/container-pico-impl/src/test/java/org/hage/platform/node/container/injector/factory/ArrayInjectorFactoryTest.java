package org.hage.platform.node.container.injector.factory;


import org.hage.platform.node.container.definition.ArrayDefinition;
import org.hage.platform.node.container.injector.ArrayInjector;
import org.junit.Test;
import org.picocontainer.ComponentAdapter;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * Tests for ArrayInjectorFactory.
 *
 * @author AGH AgE Team
 */
public class ArrayInjectorFactoryTest {

    private final ArrayInjectorFactory factory = new ArrayInjectorFactory();

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullDefinition() {
        // when
        factory.createAdapter(null);
    }

    @Test
    public void shouldCreateArrayInjectors() {
        // given
        final ArrayDefinition definition = new ArrayDefinition("any", Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(ArrayInjector.class)));
    }
}
