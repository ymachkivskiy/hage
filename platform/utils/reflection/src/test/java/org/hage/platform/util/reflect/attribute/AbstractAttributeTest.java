package org.hage.platform.util.reflect.attribute;


import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class AbstractAttributeTest {

    private static final String NAME = "name";

    @Test(expected = NullPointerException.class)
    public void testNullNameThrowsNPE() {
        // given
        final String name = null;
        final Class<String> type = String.class;

        // when
        new MockAttribute<String>(name, type);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTypeThrowsNPE() {
        // given
        final String name = NAME;
        final Class<String> type = null;

        // when
        new MockAttribute<String>(name, type);
    }

    @Test
    public void testName() {
        // given
        final String name = NAME;
        final Class<String> type = String.class;
        final Attribute<String> attribute = new MockAttribute<String>(name, type);

        // then
        assertEquals(name, attribute.getName());
    }

    @Test
    public void testType() {
        // given
        final String name = NAME;
        final Class<String> type = String.class;
        final Attribute<String> attribute = new MockAttribute<String>(name, type);

        // then
        assertEquals(type, attribute.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAsType() {
        // given
        final String name = NAME;
        final Class<Integer> sourceType = Integer.class;
        final Class<Number> targetType = Number.class;
        final Attribute<Integer> attribute = new MockAttribute<Integer>(name, sourceType);

        // then
        attribute.asType(targetType);
    }

    @Test
    public void testValidAsType() {
        // given
        final String name = NAME;
        final Class<Number> sourceType = Number.class;
        final Class<Integer> targetType = Integer.class;
        final Attribute<Number> attribute = new MockAttribute<Number>(name, sourceType);

        // then
        assertEquals(attribute, attribute.asType(targetType));
    }

    private static final class MockAttribute<T> extends AbstractAttribute<T> {

        MockAttribute(final String name, final Class<T> type) {
            super(name, type);
        }

        @Override
        public void setValue(final Object target, final T value) throws IllegalAccessException {
            throw new UnsupportedOperationException();
        }
    }

}
