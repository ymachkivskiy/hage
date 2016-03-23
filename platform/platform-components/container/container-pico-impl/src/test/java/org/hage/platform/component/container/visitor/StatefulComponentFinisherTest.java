package org.hage.platform.component.container.visitor;


import org.hage.platform.component.container.PicoInstanceContainer;
import org.hage.platform.component.container.PicoMutableInstanceContainer;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.hage.platform.component.container.visitor.StatefulComponentInitializerTest.Stateful;
import org.hage.platform.component.container.visitor.StatefulComponentInitializerTest.StatefulRegistry;
import org.junit.Test;
import org.picocontainer.PicoVisitor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class StatefulComponentFinisherTest {

    @Test
    public void shouldCallFinishOnSingletons() {
        // given
        final PicoInstanceContainer provider = new PicoInstanceContainer();
        final StatefulRegistry registry = new StatefulRegistry();
        provider.addComponentInstance(registry);
        provider.addComponent(new ComponentDefinition("singleton", Stateful.class, true));
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
        final StatefulRegistry registry = new StatefulRegistry();
        provider.addComponentInstance(registry);
        provider.addComponent(new ComponentDefinition("nonsingleton", Stateful.class, false));
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
        final StatefulRegistry registry = new StatefulRegistry();
        final PicoInstanceContainer parent = new PicoInstanceContainer();
        final PicoMutableInstanceContainer child = parent.makeChildContainer();
        parent.addComponentInstance(registry);
        child.addComponent(new ComponentDefinition("singleton", Stateful.class, true));
        final PicoVisitor visitor = new StatefulComponentFinisher();

        // when
        parent.accept(visitor);

        // then
        assertThat(registry.finishes.size(), is(1));
    }
}
