package org.hage.platform.node.container.injector;


import org.hage.platform.node.container.*;
import org.hage.platform.node.container.definition.ComponentDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.Injector;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ComponentInstanceProviderAwareInjectorTest extends AbstractBaseInjectorTest {

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
        return new ComponentInstanceProviderAwareInjector<T>(definition);
    }

    @Test
    public void shouldDoNothingIfInterfacesNotImplemented() {
        // given
        final Object instance = mock(Object.class);
        final Injector<Object> injector = injectorFor(instance);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        verifyNoMoreInteractions(instance);
    }

    @Test
    public void shouldInjectIComponentInstanceProviderAware() {
        // given
        final InstanceContainerAware instance = mock(InstanceContainerAware.class);
        final Injector<InstanceContainerAware> injector = injectorFor(instance);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        verify(instance).setInstanceProvider(container);
    }

    @Test
    public void shouldInjectIMutableComponentInstanceProviderAware() {
        // given
        final MutableInstanceContainerAware instance = mock(MutableInstanceContainerAware.class);
        final Injector<MutableInstanceContainerAware> injector = injectorFor(instance);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        verify(instance).setMutableInstanceContainer(container);
    }

    @Test
    public void shouldInjectISelfAwareComponentInstanceProviderAware() {
        // given
        final SelfAwareInstanceContainerAware instance = mock(SelfAwareInstanceContainerAware.class);
        final ComponentDefinition definition = definitionFor(instance);
        final Injector<SelfAwareInstanceContainerAware> injector = injectorFor(definition);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        final ArgumentCaptor<SelfAwareInstanceContainer> argument = ArgumentCaptor.forClass(SelfAwareInstanceContainer.class);
        verify(instance).setSelfAwareInstanceContainer(argument.capture());
        final SelfAwareInstanceContainer provider = argument.getValue();
        assertThat(provider.getName(), is(equalTo(definition.getName())));

        // when
        provider.getInstance();
        // then
        verify(container).getInstance(definition.getName());

    }
}
