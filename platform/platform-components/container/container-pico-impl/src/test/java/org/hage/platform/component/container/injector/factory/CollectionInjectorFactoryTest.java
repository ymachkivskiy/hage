package org.hage.platform.component.container.injector.factory;


import org.hage.platform.component.container.definition.CollectionDefinition;
import org.hage.platform.component.container.injector.CollectionInjector;
import org.junit.Test;
import org.picocontainer.ComponentAdapter;

import java.util.Set;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * Tests for CollectionInjectorFactory.
 *
 * @author AGH AgE Team
 */
public class CollectionInjectorFactoryTest {

    private final CollectionInjectorFactory factory = new CollectionInjectorFactory();

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullDefinition() {
        // when
        factory.createAdapter(null);
    }

    @Test
    public void shouldCreateArrayInjectors() {
        // given
        final CollectionDefinition definition = new CollectionDefinition("any", Set.class, Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(CollectionInjector.class)));
    }
}
