/**
 * Copyright (C) 2006 - 2012
 *   Pawel Kedzior
 *   Tomasz Kmiecik
 *   Kamil Pietak
 *   Krzysztof Sikora
 *   Adam Wos
 *   Lukasz Faber
 *   Daniel Krzywicki
 *   and other students of AGH University of Science and Technology.
 *
 * This file is part of AgE.
 *
 * AgE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgE.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created: 2012-03-15
 * $Id$
 */

package org.hage.platform.component.pico.injector;


import org.hage.platform.component.definition.ComponentDefinition;
import org.hage.platform.component.definition.ValueDefinition;
import org.hage.platform.component.pico.IPicoComponentInstanceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.picocontainer.Injector;
import org.picocontainer.PicoCompositionException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;


/**
 * Tests for PropertiesInjector.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class PropertiesInjectorTest extends AbstractBaseInjectorTest {

    private static final String PROPERTY = "property";

    private static final String VALUE = "value";

    @Mock
    private IPicoComponentInstanceProvider container;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullDefinition() {
        // when
        new PropertiesInjector<Object>(null);
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
        return new PropertiesInjector<T>(definition);
    }

    @Test
    public void shouldDoNothingWhenNoArguments() {
        // given
        final Object instance = mock(Object.class);
        final Injector<Object> injector = injectorFor(instance);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        verifyZeroInteractions(instance);
    }

    @Test
    public void shouldInjectArgumentInPublicSetter() throws Exception {
        // given
        final PublicSetter instance = new PublicSetter();
        final ComponentDefinition definition = definitionFor(instance);
        definition.addPropertyArgument(PROPERTY, argumentFor(VALUE));
        final Injector<PublicSetter> injector = injectorFor(definition);

        // when
        injector.decorateComponentInstance(container, null, instance);

        // then
        assertThat(instance.property, is(equalTo(VALUE)));
    }

    private ValueDefinition argumentFor(final Object value) {
        return new ValueDefinition(value.getClass(), value.toString());
    }

    @Test(expected = PicoCompositionException.class)
    public void shouldNotInjectArgumentInProtectedSetter() {
        final ProtectedSetter instance = new ProtectedSetter();
        final ComponentDefinition definition = definitionFor(instance);
        definition.addPropertyArgument(PROPERTY, argumentFor(VALUE));
        final Injector<ProtectedSetter> injector = injectorFor(definition);

        // when
        injector.decorateComponentInstance(container, null, instance);
    }

    @Test(expected = PicoCompositionException.class)
    public void shouldNotInjectArgumentInPackageSetter() {
        final PackageSetter instance = new PackageSetter();
        final ComponentDefinition definition = definitionFor(instance);
        definition.addPropertyArgument(PROPERTY, argumentFor(VALUE));
        final Injector<PackageSetter> injector = injectorFor(definition);

        // when
        injector.decorateComponentInstance(container, null, instance);
    }

    @Test(expected = PicoCompositionException.class)
    public void shouldNotInjectArgumentInPrivateSetter() {
        final PrivateSetter instance = new PrivateSetter();
        final ComponentDefinition definition = definitionFor(instance);
        definition.addPropertyArgument(PROPERTY, argumentFor(VALUE));
        final Injector<PrivateSetter> injector = injectorFor(definition);

        // when
        injector.decorateComponentInstance(container, null, instance);
    }

    @Test(expected = PicoCompositionException.class)
    public void shouldThrowExceptionIfSetterThrowException() {
        final ExceptionalSetter instance = new ExceptionalSetter();
        final ComponentDefinition definition = definitionFor(instance);
        definition.addPropertyArgument(PROPERTY, argumentFor(VALUE));
        final Injector<ExceptionalSetter> injector = injectorFor(definition);

        // when
        injector.decorateComponentInstance(container, null, instance);
    }

    @Test(expected = PicoCompositionException.class)
    public void shouldNotInjectArgumentInField() {
        final NoSetter instance = new NoSetter();
        final ComponentDefinition definition = definitionFor(instance);
        definition.addPropertyArgument(PROPERTY, argumentFor(VALUE));
        final Injector<NoSetter> injector = injectorFor(definition);

        // when
        injector.decorateComponentInstance(container, null, instance);
    }


    /**
     * Helper test class.
     *
     * @author AGH AgE Team
     */
    public static class PublicSetter {

        private String property;

        public void setProperty(final String property) {
            this.property = property;
        }
    }


    /**
     * Helper test class.
     *
     * @author AGH AgE Team
     */
    public static class ProtectedSetter {

        @SuppressWarnings("unused")
        private String property;

        protected void setProperty(final String property) {
            this.property = property;
        }
    }


    /**
     * Helper test class.
     *
     * @author AGH AgE Team
     */
    public static class PackageSetter {

        @SuppressWarnings("unused")
        private String property;

        void setProperty(final String property) {
            this.property = property;
        }
    }


    /**
     * Helper test class.
     *
     * @author AGH AgE Team
     */
    public static class PrivateSetter {

        @SuppressWarnings("unused")
        private String property;

        @SuppressWarnings("unused")
        private void setProperty(final String property) {
            this.property = property;
        }
    }


    /**
     * Helper test class.
     *
     * @author AGH AgE Team
     */
    public static class ExceptionalSetter {

        @SuppressWarnings("unused")
        private String property;

        public void setProperty(final String property) {
            throw new RuntimeException();
        }
    }


    /**
     * Helper test class.
     *
     * @author AGH AgE Team
     */
    public static class NoSetter {

        @SuppressWarnings("unused")
        private String property;
    }
}
