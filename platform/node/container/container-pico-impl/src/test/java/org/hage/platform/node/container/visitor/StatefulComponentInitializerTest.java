package org.hage.platform.node.container.visitor;


import com.google.common.collect.Lists;
import org.hage.platform.node.container.PicoInstanceContainer;
import org.hage.platform.node.container.PicoMutableInstanceContainer;
import org.hage.platform.node.container.exception.ComponentException;
import org.junit.Test;
import org.picocontainer.PicoVisitor;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class StatefulComponentInitializerTest {

    @Test
    public void shouldLookupInRootContainer() {
        // given
        final PicoInstanceContainer provider = new PicoInstanceContainer();
        final StatefulRegistry registry = new StatefulRegistry();
        provider.addComponentInstance(registry);
        provider.addSingletonComponent(Stateful.class);
        final PicoVisitor visitor = new StatefulComponentInitializer();

        // when
        provider.accept(visitor);

        // then
        assertThat(registry.inits.size(), is(1));
    }

    @Test
    public void shouldLookupInNestedContainer() {
        // given
        final StatefulRegistry registry = new StatefulRegistry();
        final PicoInstanceContainer parent = new PicoInstanceContainer();
        final PicoMutableInstanceContainer child = parent.makeChildContainer();
        parent.addComponentInstance(registry);
        child.addSingletonComponent(Stateful.class);
        final PicoVisitor visitor = new StatefulComponentInitializer();

        // when
        parent.accept(visitor);

        // then
        assertThat(registry.inits.size(), is(1));
    }

    @Test
    public void statefulProperlyRegisters() {
        // given
        final PicoInstanceContainer provider = new PicoInstanceContainer();
        final StatefulRegistry registry = new StatefulRegistry();
        provider.addComponentInstance(registry);
        provider.addSingletonComponent(Stateful.class);

        // when
        final Stateful stateful = provider.getComponent(Stateful.class);

        // then
        assertThat(registry.inits.size(), is(1));
        assertTrue(registry.inits.contains(stateful));
    }

    /**
     * Stateful instances register in this component, so that we know that their init and finish has been called.
     *
     * @author AGH AgE Team
     */
    public static class StatefulRegistry {

        public final List<Stateful> inits = Lists.newArrayList();
        public final List<Stateful> finishes = Lists.newArrayList();

        public void registerInit(final Stateful stateful) {
            inits.add(stateful);
        }

        public void registerFinish(final Stateful stateful) {
            finishes.add(stateful);
        }
    }


    public static class Stateful implements org.hage.platform.simulation.container.Stateful {

        @Inject
        private StatefulRegistry registry;

        @Override
        public void init() throws ComponentException {
            registry.registerInit(this);
        }

        @Override
        public boolean finish() throws ComponentException {
            registry.registerFinish(this);
            return false;
        }
    }
}
