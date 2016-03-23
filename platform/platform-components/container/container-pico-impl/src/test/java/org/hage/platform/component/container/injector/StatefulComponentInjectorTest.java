package org.hage.platform.component.container.injector;


import org.hage.platform.component.container.PicoMutableInstanceContainer;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.hage.platform.component.container.exception.ComponentException;
import org.hage.platform.simulation.container.Stateful;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.Injector;
import org.picocontainer.PicoCompositionException;

import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class StatefulComponentInjectorTest extends AbstractBaseInjectorTest {

    @Mock
    private PicoMutableInstanceContainer container;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullDefinition() {
        // when
        new StatefulComponentInjector<Object>(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotSupportCreatingComponents() {
        // given
        final Injector<Object> injector = injectorFor(anyDefinition());

        // when
        injector.getComponentInstance(container, null);
    }

    @Override
    protected <T> Injector<T> injectorFor(final ComponentDefinition definition) {
        return new StatefulComponentInjector<T>(definition);
    }

    @Test
    public void shouldDoNothingIfNotStatefulComponent() {
        // given
        final Object instance = mock(Object.class);
        final Injector<Object> injector = injectorFor(instance);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        verifyZeroInteractions(instance);
    }

    @Test
    public void shouldCallInitOnStatefulComponent() throws ComponentException {
        // given
        final Stateful instance = mock(Stateful.class);
        final Injector<Stateful> injector = injectorFor(instance);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        verify(instance).init();
    }

    @Test(expected = PicoCompositionException.class)
    public void shouldPropagateStatefulComponentExceptions() throws ComponentException {
        // given
        final Stateful instance = mock(Stateful.class);
        final Injector<Stateful> injector = injectorFor(instance);
        willThrow(ComponentException.class).given(instance).init();

        // when
        injector.decorateComponentInstance(container, null, instance);
    }
}
