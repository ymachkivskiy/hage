package org.hage.platform.component.container.injector.factory;


import org.hage.platform.component.container.definition.MapDefinition;
import org.hage.platform.component.container.injector.MapInjector;
import org.junit.Test;
import org.picocontainer.ComponentAdapter;

import java.util.HashMap;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * Tests for MapInjectorFactory.
 *
 * @author AGH AgE Team
 */
public class MapInjectorFactoryTest {

    private final MapInjectorFactory factory = new MapInjectorFactory();

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullDefinition() {
        // when
        factory.createAdapter(null);
    }

    @Test
    public void shouldCreateArrayInjectors() {
        // given
        final MapDefinition definition = new MapDefinition("any", HashMap.class, Object.class, Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(MapInjector.class)));
    }
}
