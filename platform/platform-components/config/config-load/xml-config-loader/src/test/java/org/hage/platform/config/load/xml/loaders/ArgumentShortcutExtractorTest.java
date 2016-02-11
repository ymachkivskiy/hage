package org.hage.platform.config.load.xml.loaders;


import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.config.load.ConfigurationNotFoundException;
import org.hage.platform.config.load.xml.ConfigTags;
import org.hage.platform.config.load.xml.util.DocumentBuilder;
import org.hage.platform.config.load.xml.util.ElementBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hage.platform.config.load.xml.ConfigAttributes.*;
import static org.hage.platform.config.load.xml.ConfigTags.ENTRY;


@RunWith(MockitoJUnitRunner.class)
public class ArgumentShortcutExtractorTest extends AbstractDocumentLoaderTest<ArgumentShortcutExtractor> {

    @Override
    public ArgumentShortcutExtractor getLoader() {
        return new ArgumentShortcutExtractor();
    }

    @Test
    public void shouldExtractConstPropValueAttr() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.constructorElement()
                             .withAttribute(TYPE, ElementBuilder.SOME_CLASS)
                             .withAttribute(VALUE, ElementBuilder.SOME_VALUE))
                .add(ElementBuilder.propertyElement(ElementBuilder.SOME_NAME)
                             .withAttribute(TYPE, ElementBuilder.SOME_CLASS)
                             .withAttribute(VALUE, ElementBuilder.SOME_VALUE));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.constructorElement(ElementBuilder.valueElement(ElementBuilder.SOME_CLASS, ElementBuilder.SOME_VALUE)))
                .add(ElementBuilder.propertyElement(ElementBuilder.SOME_NAME, ElementBuilder.valueElement(ElementBuilder.SOME_CLASS, ElementBuilder.SOME_VALUE)));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractConstPropRefAttr() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.constructorElement()
                             .withAttribute(REF, ElementBuilder.SOME_VALUE))
                .add(ElementBuilder.propertyElement(ElementBuilder.SOME_NAME)
                             .withAttribute(REF, ElementBuilder.SOME_VALUE));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.constructorElement(ElementBuilder.referenceElement(ElementBuilder.SOME_VALUE)))
                .add(ElementBuilder.propertyElement(ElementBuilder.SOME_NAME, ElementBuilder.referenceElement(ElementBuilder.SOME_VALUE)));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfConstPropBothAttrs() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.constructorElement()
                             .withAttribute(VALUE, ElementBuilder.SOME_VALUE)
                             .withAttribute(REF, ElementBuilder.SOME_VALUE));
        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfConstPropValueAttrAndContent() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.constructorElement()
                             .withAttribute(TYPE, ElementBuilder.SOME_CLASS)
                             .withAttribute(VALUE, ElementBuilder.SOME_VALUE)
                             .withBody(ElementBuilder.anyElement()));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfConstPropRefAttrAndContent() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.constructorElement()
                             .withAttribute(REF, ElementBuilder.SOME_VALUE)
                             .withBody(ElementBuilder.anyElement()));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfConstPropNoAttrsAndNoContent() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.constructorElement());

        // when
        tryDocumentTransformation(original);
    }

    @Test
    public void shouldExtractEntryKeyAttr() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String key = "key";
        final String value = "value";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ENTRY)
                                .withAttribute(KEY, key)
                                .withBody(ElementBuilder.valueElement(value))));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.mapEntryElement(ElementBuilder.valueElement(key), ElementBuilder.valueElement(value))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractEntryKeyRefAttr() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String keyRef = "key-ref";
        final String value = "value";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY_REF, keyRef)
                                .withBody(ElementBuilder.valueElement(value))));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.mapEntryElement(ElementBuilder.referenceElement(keyRef), ElementBuilder.valueElement(value))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractEntryValueAttr() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String key = "key";
        final String value = "value";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(VALUE, value)
                                .withBody(ElementBuilder.keyElement(ElementBuilder.valueElement(key)))));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.mapEntryElement(ElementBuilder.valueElement(key), ElementBuilder.valueElement(value))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractEntryValueRefAttr() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String key = "key";
        final String valueRef = "value-ref";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(VALUE_REF, valueRef)
                                .withBody(ElementBuilder.keyElement(ElementBuilder.valueElement(key)))));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.mapEntryElement(ElementBuilder.valueElement(key), ElementBuilder.referenceElement(valueRef))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldExtractEntryAllAttr() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String key = "key";
        final String keyRef = "key-ref";
        final String value = "value";
        final String valueRef = "value-ref";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(VALUE, value),
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(VALUE_REF, valueRef),
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY_REF, keyRef)
                                .withAttribute(VALUE, value),
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY_REF, keyRef)
                                .withAttribute(VALUE_REF, valueRef)));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.mapEntryElement(ElementBuilder.valueElement(key), ElementBuilder.valueElement(value)),
                        ElementBuilder.mapEntryElement(ElementBuilder.valueElement(key), ElementBuilder.referenceElement(valueRef)),
                        ElementBuilder.mapEntryElement(ElementBuilder.referenceElement(keyRef), ElementBuilder.valueElement(value)),
                        ElementBuilder.mapEntryElement(ElementBuilder.referenceElement(keyRef), ElementBuilder.referenceElement(valueRef))));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryKeyBothAttrs() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String key = "key";
        final String keyRef = "key-ref";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(KEY_REF, keyRef)));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryKeyAttrAndContent() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String key = "key";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withBody(ElementBuilder.keyElement(ElementBuilder.anyElement()))));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryKeyRefAttrAndContent() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String keyRef = "key-ref";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY_REF, keyRef)
                                .withBody(ElementBuilder.keyElement(ElementBuilder.anyElement()))));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryKeyNoAttrsAndNoContent() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryValueBothAttrs() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String key = "key";
        final String value = "value";
        final String valueRef = "value-ref";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(VALUE, value)
                                .withAttribute(VALUE_REF, valueRef)));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryValueAttrAndContent() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String key = "key";
        final String value = "value";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(VALUE, value)
                                .withBody(ElementBuilder.anyElement())));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryValueRefAttrAndContent() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String key = "key";
        final String valueRef = "value-ref";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)
                                .withAttribute(VALUE_REF, valueRef)
                                .withBody(ElementBuilder.anyElement())));

        // when
        tryDocumentTransformation(original);
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfEntryValueNoAttrsAndNoContent() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final String key = "key";
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.mapElement().withBody(
                        ElementBuilder.element(ConfigTags.ENTRY)
                                .withAttribute(KEY, key)));

        // when
        tryDocumentTransformation(original);
    }
}
