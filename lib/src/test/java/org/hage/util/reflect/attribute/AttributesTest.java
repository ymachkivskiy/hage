package org.hage.util.reflect.attribute;


import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class AttributesTest {

    @Test
    public void testFieldAttribute() throws Exception {
        // given
        final Field field = new Target().getClass().getDeclaredField("field");
        final Attribute<String> expected = FieldAttribute.newFieldAttribute(field);
        // when
        final Attribute<String> attribute = Attributes.forField(field);

        // then
        assertThat(attribute, is(equalTo(expected)));
    }

    @Test
    public void testMethodAttribute() throws Exception {
        // given
        final Method setter = new Target().getClass().getDeclaredMethod("setField", String.class);
        final Attribute<String> expected = MethodAttribute.newSetterAttribute(setter);
        // when
        final Attribute<String> attribute = Attributes.forSetter(setter);

        // then
        assertThat(attribute, is(equalTo(expected)));
    }

    private static class Target {

        @SuppressWarnings("unused")
        private String field;

        @SuppressWarnings("unused")
        private void setField(final String field) {
        }
    }
}
