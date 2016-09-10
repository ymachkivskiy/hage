package org.hage.platform.component.container.injector;


import org.hage.platform.component.container.PicoMutableInstanceContainer;
import org.hage.platform.component.container.definition.ComponentDefinition;
import org.hage.platform.component.container.definition.ValueDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.Injector;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.adapters.InstanceAdapter;

import javax.inject.Inject;
import java.lang.reflect.Type;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;


@RunWith(MockitoJUnitRunner.class)
public class ConstructorInjectorTest extends AbstractBaseInjectorTest {

    @Mock
    private PicoMutableInstanceContainer container;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullDefinition() {
        // when
        new ConstructorInjector<Object>(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotSupportDecoratingComponents() {
        // given
        final Object instance = new Object();
        final Injector<Object> injector = injectorFor(instance);

        // when
        injector.decorateComponentInstance(container, null, instance);
    }

    @Test(expected = PicoCompositionException.class)
    public void testNoMatchingArguments() throws Exception {
        // given
        final ComponentDefinition definition = definitionFor(DefaultConstructor.class);
        definition.addConstructorArgument(new ValueDefinition(String.class, "value"));
        final Injector<DefaultConstructor> injector = injectorFor(definition);

        // when
        injector.getComponentInstance(container, null);
    }

    @Override
    protected <T> Injector<T> injectorFor(final ComponentDefinition definition) {
        return new ConstructorInjector<T>(definition);
    }

    @Test(expected = PicoCompositionException.class)
    public void testMultipleMatchingArguments() throws Exception {
        // given
        final ComponentDefinition definition = definitionFor(MultipleMatchingArguments.class);
        definition.addConstructorArgument(new ValueDefinition(String.class, "value"));
        final Injector<MultipleMatchingArguments> injector = injectorFor(definition);

        // when
        injector.getComponentInstance(container, null);
    }

    @Test
    public void testSingleMatchingArgument() throws Exception {
        // given
        final ComponentDefinition definition = definitionFor(SingleMatchingArgument.class);
        final String value = "value";
        definition.addConstructorArgument(new ValueDefinition(String.class, value));
        final Injector<SingleMatchingArgument> injector = injectorFor(definition);

        // when
        final SingleMatchingArgument instance = injector.getComponentInstance(container, null);

        // then
        assertNotNull(instance);
        assertThat(instance.value, is(equalTo(value)));
    }

    @Test
    public void testSingleMatchingSuperArgument() throws Exception {
        // given
        final ComponentDefinition definition = definitionFor(SingleMatchingSuperArgument.class);
        final String value = "value";
        definition.addConstructorArgument(new ValueDefinition(String.class, value));
        final Injector<SingleMatchingSuperArgument> injector = injectorFor(definition);

        // when
        final SingleMatchingSuperArgument instance = injector.getComponentInstance(container, null);

        // then
        assertNotNull(instance);
        assertThat(instance.value, is(instanceOf(String.class)));
        assertThat(instance.value, is(equalTo(value)));
    }

    @Test
    public void testSingleMatchingPrimitiveArgument() throws Exception {
        // given
        final ComponentDefinition definition = definitionFor(SingleMatchingPrimitiveArgument.class);
        final String value = "123";
        definition.addConstructorArgument(new ValueDefinition(Integer.class, value));
        final Injector<SingleMatchingPrimitiveArgument> injector = injectorFor(definition);

        // when
        final SingleMatchingPrimitiveArgument instance = injector.getComponentInstance(container, null);

        // then
        assertNotNull(instance);
        assertThat(instance.value, is(equalTo(Integer.parseInt(value))));
    }

    @Test(expected = PicoCompositionException.class)
    public void testMultipleAnnotation() throws Exception {
        // given
        final Injector<MultipleAnnotation> injector = injectorFor(MultipleAnnotation.class);

        // when
        injector.getComponentInstance(container, null);
    }

    @Test
    public void testSingleAnnotationNoDependencies() throws Exception {
        // given
        final Injector<SingleAnnotationNoDependencies> injector = injectorFor(SingleAnnotationNoDependencies.class);

        // when
        final SingleAnnotationNoDependencies instance = injector.getComponentInstance(container, null);

        // then
        assertNotNull(instance);
    }

    @Test(expected = PicoCompositionException.class)
    public void testSingleAnnotationWithDependencyUnsatisfied() throws Exception {
        // given
        final Injector<SingleAnnotationWithDependency> injector = injectorFor(SingleAnnotationWithDependency.class);

        // when
        injector.getComponentInstance(container, null);
    }

    @Test
    public void testSingleAnnotationWithDependency() throws Exception {
        // given
        final Injector<SingleAnnotationWithDependency> injector = injectorFor(SingleAnnotationWithDependency.class);
        final Object dependency = new Object();
        providerHasComponent(Object.class, dependency);

        // when
        final SingleAnnotationWithDependency instance = injector.getComponentInstance(container, null);

        // then
        assertNotNull(instance);
        assertThat(instance.dependency, is(sameInstance(dependency)));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T> void providerHasComponent(final Class<T> key, final T component) {
        given(container.getComponent(eq(key), any(Type.class))).willReturn(component);
        given(container.getComponentAdapter(key)).willReturn(new InstanceAdapter(key, component));
    }

    @Test(expected = PicoCompositionException.class)
    public void testNoDefaultConstructor() throws Exception {
        // given
        final Injector<NoDefaultConstructor> injector = injectorFor(NoDefaultConstructor.class);

        // when
        injector.getComponentInstance(container, null);
    }

    @Test
    public void testDefaultConstructor() throws Exception {
        // given
        final Injector<DefaultConstructor> injector = injectorFor(DefaultConstructor.class);

        // when
        final DefaultConstructor instance = injector.getComponentInstance(container, null);

        // then
        assertNotNull(instance);
    }


    @SuppressWarnings("unused")
    private static class MultipleMatchingArguments {

        public MultipleMatchingArguments(final Object value) {
        }

        public MultipleMatchingArguments(final String value) {
        }
    }


    @SuppressWarnings("unused")
    private static class SingleMatchingArgument {

        private final String value;

        public SingleMatchingArgument(final String value) {
            this.value = value;
        }
    }


    @SuppressWarnings("unused")
    private static class SingleMatchingSuperArgument {

        private final Object value;

        public SingleMatchingSuperArgument(final Object value) {
            this.value = value;
        }
    }


    @SuppressWarnings("unused")
    private static class SingleMatchingPrimitiveArgument {

        private final int value;

        public SingleMatchingPrimitiveArgument(final int value) {
            this.value = value;
        }
    }


    @SuppressWarnings("unused")
    private static class MultipleAnnotation {

        @Inject
        public MultipleAnnotation() {
        }

        @Inject
        public MultipleAnnotation(final String foo) {
        }
    }


    @SuppressWarnings("unused")
    private static class SingleAnnotationNoDependencies {

        @Inject
        public SingleAnnotationNoDependencies() {
        }
    }


    @SuppressWarnings("unused")
    private static class SingleAnnotationWithDependency {

        private final Object dependency;

        @Inject
        public SingleAnnotationWithDependency(final Object dependency) {
            this.dependency = dependency;
        }
    }


    @SuppressWarnings("unused")
    private static class NoDefaultConstructor {

        public NoDefaultConstructor(final String foo) {
        }
    }


    private static class DefaultConstructor {

    }
}
