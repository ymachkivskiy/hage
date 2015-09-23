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
 * Created: 2012-01-28
 * $Id$
 */

package org.jage.platform.config.xml.loaders;


import org.dom4j.Document;
import org.dom4j.util.NodeComparator;
import org.jage.platform.component.definition.ConfigurationException;
import org.junit.Test;

import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.jage.platform.config.xml.util.DocumentBuilder.emptyDocument;
import static org.jage.platform.config.xml.util.ElementBuilder.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;


/**
 * Unit tests for DocumentResolver.
 *
 * @author AGH AgE Team
 */
public class DocumentResolverTest extends AbstractDocumentLoaderTest<DocumentResolver> {

    private final String includingPath = "includingPath";

    private final String includedPath = "includedPath";

    private final String blockName = "block";

    private final String innerName = "inner";

    @Override
    protected DocumentResolver getLoader() {
        return new DocumentResolver();
    }

    @Test(timeout = 100, expected = ConfigurationException.class)
    public void shouldThrowExcAtCyclicDependency() throws ConfigurationException {
        // given
        final int cycleLength = 15;
        final Map<String, Document> cyclicDocuments = createCyclicDocuments(includingPath, cycleLength);
        for(final Entry<String, Document> e : cyclicDocuments.entrySet()) {
            given(delegate.loadDocument(e.getKey())).willReturn(e.getValue());
        }

        // when
        loader.loadDocument(includingPath);
    }

    private Map<String, Document> createCyclicDocuments(final String baseName, final int length) {
        checkArgument(length > 1);
        final Map<String, Document> documents = newLinkedHashMap();

        String nextName = baseName;
        String currentName;

        for(int i = 0; i < length - 1; i++) {
            currentName = nextName;
            nextName = baseName + i;
            documents.put(currentName, createCyclicDocument(nextName));
        }

        currentName = nextName;
        nextName = baseName;
        documents.put(currentName, createCyclicDocument(nextName));

        return documents;
    }

    private Document createCyclicDocument(final String nextDocumentPath) {
        return emptyDocument().add(includeElement(nextDocumentPath)).build();
    }

    @Test
    public void shouldBeReentrantAfterCyclicDependency() throws ConfigurationException {
        // given
        final int cycleLength = 15;
        final Map<String, Document> cyclicDocuments = createCyclicDocuments(includingPath, cycleLength);
        for(final Entry<String, Document> e : cyclicDocuments.entrySet()) {
            given(delegate.loadDocument(e.getKey())).willReturn(e.getValue());
        }

        // when
        try {
            loader.loadDocument(includingPath);
        } catch(final ConfigurationException swallowed) {
        }

        // then given
        final Document original = emptyDocument().build();
        given(delegate.loadDocument(includingPath)).willReturn(original);

        // when
        final Document document = loader.loadDocument(includingPath);

        // then
        assertEquals(0, new NodeComparator().compare(original, document));
    }

    @Test
    public void shouldIncludeUnchangedDocument() throws ConfigurationException {
        given(delegate.loadDocument(includingPath)).willReturn(createIncludingDocument(includedPath));
        given(delegate.loadDocument(includedPath)).willReturn(createIncludedDocument());
        final Document expectedDocument = createFullDocument();

        // when
        final Document document = loader.loadDocument(includingPath);

        // then
        assertEquals(0, new NodeComparator().compare(expectedDocument, document));
    }

    private Document createIncludingDocument(final String path) {
        return emptyDocument()
                .add(arrayElement())
                .add(includeElement(path))
                .add(listElement())
                .build();
    }

    private Document createIncludedDocument() {
        return emptyDocument()
                .add(componentElement())
                .add(blockElement(SOME_NAME)
                             .withBody(componentElement()))
                .add(mapElement())
                .build();
    }

    private Document createFullDocument() {
        return emptyDocument()
                .add(arrayElement())
                .add(componentElement())
                .add(blockElement(SOME_NAME)
                             .withBody(componentElement()))
                .add(mapElement())
                .add(listElement())
                .build();
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfMultipleBlockDefinition() throws ConfigurationException {
        // given
        given(delegate.loadDocument(includingPath)).willReturn(createMultipleBlockDefinitionDocument());

        // when
        loader.loadDocument(includingPath);
    }

    private Document createMultipleBlockDefinitionDocument() {
        return emptyDocument()
                .add(blockElement(SOME_NAME))
                .add(blockElement(SOME_NAME))
                .build();
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcAIfMultipleBlockOverride() throws ConfigurationException {
        // given
        given(delegate.loadDocument(includingPath)).willReturn(createMultipleBlockOverrideDocument());

        // when
        loader.loadDocument(includingPath);
    }

    private Document createMultipleBlockOverrideDocument() {
        return emptyDocument()
                .add(includeElement(SOME_NAME)
                             .withBody(
                                     blockElement(SOME_NAME),
                                     blockElement(SOME_NAME)))
                .build();
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfUnknownBlockOverride() throws ConfigurationException {
        // given
        given(delegate.loadDocument(includingPath)).willReturn(createUnknownBlockOverrideDocument(includedPath));
        given(delegate.loadDocument(includedPath)).willReturn(emptyDocument().build());

        // when
        loader.loadDocument(includingPath);
    }

    private Document createUnknownBlockOverrideDocument(final String path) {
        return emptyDocument()
                .add(includeElement(path)
                             .withBody(blockElement(SOME_NAME)))
                .build();
    }

    @Test(expected = ConfigurationException.class)
    public void shouldThrowExcIfRedefiningBlock() throws ConfigurationException {
        // given
        given(delegate.loadDocument(includingPath)).willReturn(createRedefiningBlockIncludingDocument(includedPath));
        given(delegate.loadDocument(includedPath)).willReturn(createRedefiningBlockIncludedDocument());

        // when
        loader.loadDocument(includingPath);
    }

    private Document createRedefiningBlockIncludingDocument(final String path) {
        return emptyDocument()
                .add(includeElement(path))
                .add(blockElement(SOME_NAME))
                .build();
    }

    private Document createRedefiningBlockIncludedDocument() {
        return emptyDocument()
                .add(blockElement(SOME_NAME))
                .build();
    }

    @Test
    public void shouldOverrideBlock() throws ConfigurationException {
        given(delegate.loadDocument(includingPath))
                .willReturn(createOverrideIncludingDocument(includedPath, blockName));
        given(delegate.loadDocument(includedPath)).
                willReturn(createOverrideIncludedDocument(blockName));
        final Document expectedDocument = createOverrideExpectedDocument(blockName);

        // when
        final Document document = loader.loadDocument(includingPath);

        // then
        assertEquals(0, new NodeComparator().compare(expectedDocument, document));
    }

    private Document createOverrideIncludingDocument(final String path, final String name) {
        return emptyDocument()
                .add(arrayElement())
                .add(includeElement(path)
                             .withBody(blockElement(name, true)
                                               .withBody(setElement())))
                .add(listElement())
                .build();
    }

    private Document createOverrideIncludedDocument(final String name) {
        return emptyDocument()
                .add(componentElement())
                .add(blockElement(name)
                             .withBody(componentElement()))
                .add(mapElement())
                .build();
    }

    private Document createOverrideExpectedDocument(final String name) {
        return emptyDocument()
                .add(arrayElement())
                .add(componentElement())
                .add(blockElement(name)
                             .withBody(setElement()))
                .add(mapElement())
                .add(listElement())
                .build();
    }

    @Test
    public void shouldExtendBlock() throws ConfigurationException {
        given(delegate.loadDocument(includingPath)).willReturn(createExtendIncludingDocument(includedPath, blockName));
        given(delegate.loadDocument(includedPath)).willReturn(createExtendIncludedDocument(blockName));
        final Document expectedDocument = createExtendExpectedDocument(blockName);

        // when
        final Document document = loader.loadDocument(includingPath);

        // then
        assertEquals(0, new NodeComparator().compare(expectedDocument, document));
    }

    private Document createExtendIncludingDocument(final String path, final String name) {
        return emptyDocument()
                .add(arrayElement())
                .add(includeElement(path)
                             .withBody(blockElement(name, false)
                                               .withBody(setElement())))
                .add(listElement())
                .build();
    }

    private Document createExtendIncludedDocument(final String name) {
        return emptyDocument()
                .add(componentElement())
                .add(blockElement(name)
                             .withBody(componentElement()))
                .add(mapElement())
                .build();
    }

    private Document createExtendExpectedDocument(final String name) {
        return emptyDocument()
                .add(arrayElement())
                .add(componentElement())
                .add(blockElement(name)
                             .withBody(
                                     componentElement(),
                                     setElement()))
                .add(mapElement())
                .add(listElement())
                .build();
    }

    @Test
    public void shouldDropInnerBlockIfOuterOverriden() throws ConfigurationException {
        given(delegate.loadDocument(includingPath))
                .willReturn(createDropInnerIncludingDocument(includedPath, blockName, innerName));
        given(delegate.loadDocument(includedPath))
                .willReturn(createDropInnerIncludedDocument(blockName, innerName));
        final Document expectedDocument = createDropInnerExpectedDocument(blockName);

        // when
        final Document document = loader.loadDocument(includingPath);

        // then
        assertEquals(0, new NodeComparator().compare(expectedDocument, document));
    }

    private Document createDropInnerIncludingDocument(final String path, final String name, final String inner) {
        return emptyDocument()
                .add(arrayElement())
                .add(includeElement(path)
                             .withBody(
                                     blockElement(name, true)
                                             .withBody(setElement()),
                                     blockElement(inner, true)
                                             .withBody(setElement())))
                .add(listElement())
                .build();
    }

    private Document createDropInnerIncludedDocument(final String name, final String inner) {
        return emptyDocument()
                .add(componentElement())
                .add(blockElement(name)
                             .withBody(
                                     componentElement(),
                                     blockElement(inner)
                                             .withBody(componentElement())))
                .add(mapElement())
                .build();
    }

    private Document createDropInnerExpectedDocument(final String name) {
        return emptyDocument()
                .add(arrayElement())
                .add(componentElement())
                .add(blockElement(name)
                             .withBody(setElement()))
                .add(mapElement())
                .add(listElement())
                .build();
    }

    @Test
    public void shouldOverrideInnerBlockIfOuterExtended() throws ConfigurationException {
        given(delegate.loadDocument(includingPath))
                .willReturn(createOverrideInnerIncludingDocument(includedPath, blockName, innerName));
        given(delegate.loadDocument(includedPath))
                .willReturn(createOverrideInnerIncludedDocument(blockName, innerName));
        final Document expectedDocument = createOverrideInnerExpectedDocument(blockName, innerName);

        // when
        final Document document = loader.loadDocument(includingPath);

        // then
        assertEquals(0, new NodeComparator().compare(expectedDocument, document));
    }

    private Document createOverrideInnerIncludingDocument(final String path, final String name, final String inner) {
        return emptyDocument()
                .add(arrayElement())
                .add(includeElement(path)
                             .withBody(
                                     blockElement(name, false)
                                             .withBody(setElement()),
                                     blockElement(inner, true)
                                             .withBody(setElement())))
                .add(listElement())
                .build();
    }

    private Document createOverrideInnerIncludedDocument(final String name, final String inner) {
        return emptyDocument()
                .add(componentElement())
                .add(blockElement(name)
                             .withBody(
                                     componentElement(),
                                     blockElement(inner)
                                             .withBody(componentElement())))
                .add(mapElement())
                .build();
    }

    private Document createOverrideInnerExpectedDocument(final String name, final String inner) {
        return emptyDocument()
                .add(arrayElement())
                .add(componentElement())
                .add(blockElement(name)
                             .withBody(
                                     componentElement(),
                                     blockElement(inner)
                                             .withBody(setElement()),
                                     setElement()))
                .add(mapElement())
                .add(listElement())
                .build();
    }
}
