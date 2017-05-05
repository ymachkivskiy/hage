package org.hage.platform.node.container.injector.factory;


import org.hage.platform.node.container.definition.*;
import org.hage.platform.node.container.injector.ArrayInjector;
import org.hage.platform.node.container.injector.CollectionInjector;
import org.hage.platform.node.container.injector.ComponentInjector;
import org.hage.platform.node.container.injector.MapInjector;
import org.junit.Test;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.behaviors.Cached;

import java.util.HashMap;
import java.util.Set;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Tests for DefaultInjectorFactory.
 *
 * @author AGH AgE Team
 */
public class DefaultInjectorFactoryTest {

    private static final String ANY = "any";

    private final DefaultInjectorFactory factory = new DefaultInjectorFactory();

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullDefinition() {
        // when
        factory.createAdapter(null);
    }

    @Test
    public void shouldRecogniseArrayDefinitions() {
        // given
        final ArrayDefinition definition = new ArrayDefinition(ANY, Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(ArrayInjector.class)));
    }

    @Test
    public void shouldRecogniseCollectionDefinitions() {
        // given
        final CollectionDefinition definition = new CollectionDefinition(ANY, Set.class, Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(CollectionInjector.class)));
    }

    @Test
    public void shouldRecogniseMapDefinitions() {
        // given
        final MapDefinition definition = new MapDefinition(ANY, HashMap.class, Object.class, Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(MapInjector.class)));
    }

    @Test
    public void shouldRecogniseComponentDefinitions() {
        // given
        final ComponentDefinition definition = new ComponentDefinition(ANY, Object.class, false);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(ComponentInjector.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnUnercognisedDefinition() {
        // given
        final IComponentDefinition definition = mock(IComponentDefinition.class);

        // when
        factory.createAdapter(definition);
    }

    @Test
    public void shouldWrapSingletonsInCachedAdapter() {
        // given
        final ComponentDefinition definition = new ComponentDefinition(ANY, Object.class, true);

        // when
        final ComponentAdapter<Object> adapter = factory.createAdapter(definition);

        // then
        assertThat(adapter, is(instanceOf(Cached.class)));
        assertThat(adapter.getDelegate(), is(instanceOf(ComponentInjector.class)));
    }
}
