package org.hage.platform.node.container.definition;


import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class ValueDefinitionTest {

    private final Class<String> desiredClass = String.class;

    private final String stringValue = "value";

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEOnNullDesiredClass() {
        // when
        new ValueDefinition(null, null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDesiredClass() {
        // given
        ValueDefinition definition = new ValueDefinition(desiredClass, stringValue);

        // then
        assertThat(definition.getDesiredClass(), is(equalTo(desiredClass)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEOnNullStringValue() {
        // when
        new ValueDefinition(desiredClass, null);
    }

    @Test
    public void testStringValue() {
        // given
        ValueDefinition definition = new ValueDefinition(desiredClass, stringValue);

        // then
        assertThat(definition.getStringValue(), is(equalTo(stringValue)));
    }
}
