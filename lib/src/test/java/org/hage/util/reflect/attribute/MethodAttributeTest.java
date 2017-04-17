package org.hage.util.reflect.attribute;


import org.hage.util.reflect.Methods;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.hage.util.reflect.Methods.getSetterName;
import static org.hage.util.reflect.attribute.MethodAttribute.newSetterAttribute;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


/**
 * Tests for MethodAttribute.
 *
 * @author AGH AgE Team
 */
public class MethodAttributeTest {

    @Test(expected = NullPointerException.class)
    public void testNullSetterThrowsNPE() {
        // when
        newSetterAttribute(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotSetterThrowsException() throws Exception {
        // given
        final Method method = new NotSetterTarget().getClass().getDeclaredMethod("notProperty");

        // when
        newSetterAttribute(method);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticSetterThrowsException() throws Exception {
        // given
        final Method method = getSetter(new StaticSetterTarget());

        // when
        newSetterAttribute(method);
    }

    private Method getSetter(final Object target) throws Exception {
        return target.getClass().getDeclaredMethod("setProperty", String.class);
    }

    @Test
    public void testAttributeInfersNameFromSetter() throws Exception {
        // given
        final Method setter = getSetter(new PrivateTarget());

        // when
        final Attribute<String> attribute = newSetterAttribute(setter);

        // then
        assertEquals(getSetterName(setter), attribute.getName());
    }

    @Test
    public void testAttributeInfersTypeFromSetter() throws Exception {
        // given
        final Method setter = getSetter(new PrivateTarget());

        // when
        final Attribute<String> attribute = newSetterAttribute(setter);

        // then
        assertEquals(Methods.getSetterType(setter), attribute.getType());
    }

    @Test
    public void testInjectionInPublicSetter() throws Exception {
        // given
        final PublicTarget target = new PublicTarget();
        final Method setter = getSetter(target);
        final Attribute<String> attribute = newSetterAttribute(setter);
        final String value = "value";

        // when
        attribute.setValue(target, value);

        // then
        assertThat(target.property, is(equalTo(value)));
    }

    @Test
    public void testInjectionInPackageSetter() throws Exception {
        // given
        final PackageTarget target = new PackageTarget();
        final Method setter = getSetter(target);
        final Attribute<String> attribute = newSetterAttribute(setter);
        final String value = "value";

        // when
        attribute.setValue(target, value);

        // then
        assertThat(target.property, is(equalTo(value)));
    }

    @Test
    public void testInjectionInPrivateSetter() throws Exception {
        // given
        final PrivateTarget target = new PrivateTarget();
        final Method setter = getSetter(target);
        final Attribute<String> attribute = newSetterAttribute(setter);
        final String value = "value";

        // when
        attribute.setValue(target, value);

        // then
        assertThat(target.property, is(equalTo(value)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapsThrownExceptions() throws Exception {
        // given
        final ThrowingTarget target = new ThrowingTarget();
        final Method setter = getSetter(target);
        final Attribute<String> attribute = newSetterAttribute(setter);
        final String value = "value";

        // when
        attribute.setValue(target, value);
    }

    @Test
    public void testEquals() throws Exception {
        // given
        final Attribute<?> attribute = newSetterAttribute(getSetter(new PrivateTarget()));
        final Attribute<?> wrongAttributeType = mock(Attribute.class);
        final Attribute<?> wrongMethod = newSetterAttribute(getSetter(new PublicTarget()));
        final Attribute<?> sameMEthod = newSetterAttribute(getSetter(new PrivateTarget()));

        // then
        assertTrue(attribute.equals(attribute));
        assertFalse(attribute.equals(null));
        assertFalse(attribute.equals(wrongAttributeType));
        assertFalse(attribute.equals(wrongMethod));
        assertTrue(attribute.equals(sameMEthod));
    }

    @Test
    public void testHashcode() throws Exception {
        // given
        final Attribute<?> attribute = newSetterAttribute(getSetter(new PrivateTarget()));
        final Attribute<?> other = newSetterAttribute(getSetter(new PrivateTarget()));

        // then
        assertEquals(attribute.hashCode(), other.hashCode());
    }


    private static class NotSetterTarget {

        @SuppressWarnings("unused")
        private void notProperty() {
        }
    }


    private static class StaticSetterTarget {

        @SuppressWarnings("unused")
        private static void setProperty(final String value) {
        }
    }


    private static class PrivateTarget {

        private String property;

        @SuppressWarnings("unused")
        private void setProperty(final String property) {
            this.property = property;
        }
    }


    private static class PackageTarget {

        private String property;

        @SuppressWarnings("unused")
        void setProperty(final String property) {
            this.property = property;
        }
    }


    private static class PublicTarget {

        private String property;

        @SuppressWarnings("unused")
        public void setProperty(final String property) {
            this.property = property;
        }
    }


    private static class ThrowingTarget {

        @SuppressWarnings("unused")
        private void setProperty(final String property) {
            throw new RuntimeException();
        }
    }
}
