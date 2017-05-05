package org.hage.platform.node.container.visitor;


import org.hage.platform.node.container.PicoInstanceContainer;
import org.hage.platform.node.container.PicoMutableInstanceContainer;
import org.hage.platform.node.container.definition.ComponentDefinition;
import org.junit.Test;
import org.picocontainer.PicoVisitor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class StatefulComponentFinisherTest {

    @Test
    public void shouldCallFinishOnSingletons() {
        // given
        final PicoInstanceContainer provider = new PicoInstanceContainer();
        final StatefulComponentInitializerTest.StatefulRegistry registry = new StatefulComponentInitializerTest.StatefulRegistry();
        provider.addComponentInstance(registry);
        provider.addComponent(new ComponentDefinition("singleton", StatefulComponentInitializerTest.Stateful.class, true));
        final PicoVisitor visitor = new StatefulComponentFinisher();

        provider.getComponent("singleton");
        registry.inits.clear();

        // when
        provider.accept(visitor);

        // then
        assertThat(registry.inits.size(), is(0));
        assertThat(registry.finishes.size(), is(1));
    }

    @Test
    public void shouldNotCallFinishOnNonSingletons() {
        // given
        final PicoInstanceContainer provider = new PicoInstanceContainer();
        final StatefulComponentInitializerTest.StatefulRegistry registry = new StatefulComponentInitializerTest.StatefulRegistry();
        provider.addComponentInstance(registry);
        provider.addComponent(new ComponentDefinition("nonsingleton", StatefulComponentInitializerTest.Stateful.class, false));
        final PicoVisitor visitor = new StatefulComponentFinisher();

        // when
        provider.accept(visitor);

        // then
        assertThat(registry.inits.size(), is(0));
        assertThat(registry.finishes.size(), is(0));
    }

    @Test
    public void shouldLookupInNestedContainer() {
        // given
        final StatefulComponentInitializerTest.StatefulRegistry registry = new StatefulComponentInitializerTest.StatefulRegistry();
        final PicoInstanceContainer parent = new PicoInstanceContainer();
        final PicoMutableInstanceContainer child = parent.makeChildContainer();
        parent.addComponentInstance(registry);
        child.addComponent(new ComponentDefinition("singleton", StatefulComponentInitializerTest.Stateful.class, true));
        final PicoVisitor visitor = new StatefulComponentFinisher();

        // when
        parent.accept(visitor);

        // then
        assertThat(registry.finishes.size(), is(1));
    }
}
