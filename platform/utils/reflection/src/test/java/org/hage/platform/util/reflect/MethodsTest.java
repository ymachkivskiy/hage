package org.hage.platform.util.reflect;


import org.junit.Test;

import java.lang.reflect.Method;

import static org.hage.platform.util.reflect.Methods.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class MethodsTest {

    private static final String NAME = "property";

    @Test
    public void testValidIsBooleanGetter() throws Exception {
        // given
        final Method booleanGetter = getBooleanGetter(new BooleanTarget());

        // then
        assertTrue(isBooleanGetter(booleanGetter));
    }

    private Method getBooleanGetter(final Object target) throws Exception {
        return target.getClass().getDeclaredMethod("isProperty");
    }

    @Test
    public void testBooleanGetterName() throws Exception {
        // given
        final Method booleanGetter = getBooleanGetter(new BooleanTarget());

        // then
        assertEquals(NAME, getGetterName(booleanGetter));
    }

    @Test
    public void testBooleanGetterType() throws Exception {
        // given
        final Method booleanGetter = getBooleanGetter(new BooleanTarget());

        // then
        assertEquals(boolean.class, getGetterType(booleanGetter));
    }

    @Test
    public void testValidIsGetter() throws Exception {
        // given
        final Method getter = getGetter(new StringTarget());

        // then
        assertTrue(isGetter(getter));
    }

    private Method getGetter(final Object target) throws Exception {
        return target.getClass().getDeclaredMethod("getProperty");
    }

    @Test
    public void testGetterName() throws Exception {
        // given
        final Method getter = getGetter(new StringTarget());

        // then
        assertEquals(NAME, getGetterName(getter));
    }

    @Test
    public void testGetterType() throws Exception {
        // given
        final Method getter = getGetter(new StringTarget());

        // then
        assertEquals(String.class, getGetterType(getter));
    }

    @Test
    public void testValidIsSetter() throws Exception {
        // given
        final Method setter = getSetter(new StringTarget());

        // then
        assertTrue(isSetter(setter));
    }

    private Method getSetter(final Object target) throws Exception {
        return target.getClass().getDeclaredMethod("setProperty", String.class);
    }

    @Test
    public void testSetterName() throws Exception {
        // given
        final Method setter = getSetter(new StringTarget());

        // then
        assertEquals(NAME, Methods.getSetterName(setter));
    }

    @Test
    public void testSetterType() throws Exception {
        // given
        final Method setter = getSetter(new StringTarget());

        // then
        assertEquals(String.class, Methods.getSetterType(setter));
    }


    private static class BooleanTarget {

        @SuppressWarnings("unused")
        public boolean isProperty() {
            return true;
        }
    }


    private static class StringTarget {

        @SuppressWarnings("unused")
        public String getProperty() {
            return null;
        }

        @SuppressWarnings("unused")
        public void setProperty(final String property) {
        }
    }
}
