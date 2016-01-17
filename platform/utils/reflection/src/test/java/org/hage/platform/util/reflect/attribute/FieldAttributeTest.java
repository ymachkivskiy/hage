package org.hage.platform.util.reflect.attribute;


import org.junit.Test;

import java.lang.reflect.Field;

import static org.hage.platform.util.reflect.attribute.FieldAttribute.newFieldAttribute;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class FieldAttributeTest {

    @Test(expected = NullPointerException.class)
    public void testNullFieldThrowsNPE() {
        // when
        newFieldAttribute(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrivateFieldThrowsIllegalArgumentException() throws Exception {
        // given
        final Field field = getField(new FinalTarget());

        // when
        newFieldAttribute(field);
    }

    private Field getField(final Object target) throws Exception {
        return target.getClass().getDeclaredField("field");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticFieldThrowsIllegalArgumentException() throws Exception {
        // given
        final Field field = getField(new StaticTarget());

        // when
        newFieldAttribute(field);
    }

    @Test
    public void testAttributeInfersNameFromField() throws Exception {
        // given
        final Field field = getField(new PrivateTarget());

        // when
        final Attribute<String> attribute = newFieldAttribute(field);

        // then
        assertEquals(field.getName(), attribute.getName());
    }

    @Test
    public void testAttributeInfersTypeFromField() throws Exception {
        // given
        final Field field = getField(new PrivateTarget());

        // when
        final Attribute<String> attribute = newFieldAttribute(field);

        // then
        assertEquals(field.getType(), attribute.getType());
    }

    @Test
    public void testInjectionInPublicField() throws Exception {
        // given
        final PublicTarget target = new PublicTarget();
        final Field field = getField(target);
        final Attribute<String> attribute = newFieldAttribute(field);
        final String value = "value";

        // when
        attribute.setValue(target, value);

        // then
        assertThat(target.field, is(equalTo(value)));
    }

    @Test
    public void testInjectionInPackageField() throws Exception {
        // given
        final PackageTarget target = new PackageTarget();
        final Field field = getField(target);
        final Attribute<String> attribute = newFieldAttribute(field);
        final String value = "value";

        // when
        attribute.setValue(target, value);

        // then
        assertThat(target.field, is(equalTo(value)));
    }

    @Test
    public void testInjectionInPrivateField() throws Exception {
        // given
        final PrivateTarget target = new PrivateTarget();
        final Field field = getField(target);
        final Attribute<String> attribute = newFieldAttribute(field);
        final String value = "value";

        // when
        attribute.setValue(target, value);

        // then
        assertThat(target.field, is(equalTo(value)));
    }

    @Test
    public void testEquals() throws Exception {
        // given
        final Attribute<?> attribute = newFieldAttribute(getField(new PrivateTarget()));
        final Attribute<?> wrongAttributeType = mock(Attribute.class);
        final Attribute<?> wrongField = newFieldAttribute(getField(new PublicTarget()));
        final Attribute<?> sameField = newFieldAttribute(getField(new PrivateTarget()));

        // then
        assertTrue(attribute.equals(attribute));
        assertFalse(attribute.equals(null));
        assertFalse(attribute.equals(wrongAttributeType));
        assertFalse(attribute.equals(wrongField));
        assertTrue(attribute.equals(sameField));
    }

    @Test
    public void testHashcode() throws Exception {
        // given
        final Attribute<?> attribute = newFieldAttribute(getField(new PrivateTarget()));
        final Attribute<?> other = newFieldAttribute(getField(new PrivateTarget()));

        // then
        assertEquals(attribute.hashCode(), other.hashCode());
    }


    private static class FinalTarget {

        @SuppressWarnings("unused")
        private final String field = "value";
    }


    private static class StaticTarget {

        @SuppressWarnings("unused")
        private static String field;
    }


    private static class PublicTarget {

        public String field;
    }


    private static class PackageTarget {

        String field;
    }


    private static class PrivateTarget {

        private String field;
    }
}
