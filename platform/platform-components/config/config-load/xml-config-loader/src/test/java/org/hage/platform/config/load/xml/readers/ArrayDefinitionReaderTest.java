package org.hage.platform.config.load.xml.readers;


import org.dom4j.Element;
import org.hage.platform.component.definition.ArrayDefinition;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.component.definition.IArgumentDefinition;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.load.xml.ConfigAttributes;
import org.hage.platform.config.load.xml.ConfigTags;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hage.platform.config.load.xml.util.ElementBuilder.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


/**
 * Unit tests for ArrayDefinitionReader.
 *
 * @author AGH AgE Team
 */
@RunWith(MockitoJUnitRunner.class)
public class ArrayDefinitionReaderTest {

    @InjectMocks
    private final ArrayDefinitionReader reader = new ArrayDefinitionReader();
    @Mock
    @SuppressWarnings("unused")
    private IDefinitionReader<IArgumentDefinition> argumentReader;
    @Mock
    @SuppressWarnings("unused")
    private IDefinitionReader<IComponentDefinition> instanceReader;

    @Test
    public void testValidBasicDefinition() throws ConfigurationException {
        // given
        final Element element = arrayElement().build();

        // when
        final ArrayDefinition definition = reader.read(element);

        // then
        assertNotNull(definition);
        assertThat(definition.getName(), is(SOME_NAME));
        assertEquals(String[].class, definition.getType());
        assertThat(definition.isSingleton(), is(true));
        assertTrue(definition.getItems().isEmpty());
    }

    @Test(expected = ConfigurationException.class)
    public void testNameAttributeIsRequired() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.ARRAY)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testNameAttributeIsNotEmpty() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.ARRAY)
                .withAttribute(ConfigAttributes.NAME, "")
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testValueTypeAttributeIsRequired() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.ARRAY)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testValueTypeAttributeIsNotEmpty() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.ARRAY)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .withAttribute(ConfigAttributes.VALUE_TYPE, "")
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testSingletonAttributeIsRequired() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.ARRAY)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .withAttribute(ConfigAttributes.VALUE_TYPE, SOME_CLASS)
                .build();

        // when
        reader.read(element);
    }

    @Test(expected = ConfigurationException.class)
    public void testSingletonAttributeIsNotEmpty() throws ConfigurationException {
        // given
        final Element element = element(ConfigTags.ARRAY)
                .withAttribute(ConfigAttributes.NAME, SOME_NAME)
                .withAttribute(ConfigAttributes.VALUE_TYPE, SOME_CLASS)
                .withAttribute(ConfigAttributes.IS_SINGLETON, "")
                .build();

        // when
        reader.read(element);
    }
}
