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

package org.hage.platform.config.xml.readers;


import com.google.common.collect.ImmutableMap;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.component.definition.IArgumentDefinition;
import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.xml.ConfigTags;
import org.hage.platform.config.xml.ConfigUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.google.common.collect.Lists.newArrayListWithCapacity;


/**
 * Reader for whole Document trees.
 * <p>
 * Uses {@link MappingDefinitionReader}s to connect suitable readers to reflect the hierarchy of component definition.
 *
 * @author AGH AgE Team
 */
public class DocumentReader {

    private final IDefinitionReader<IComponentDefinition> reader;

    /**
     * Creates a DocumentReader.
     */
    public DocumentReader() {
        this(wireUpReaders());
    }

    DocumentReader(final IDefinitionReader<IComponentDefinition> reader) {
        this.reader = reader;
    }

    private static IDefinitionReader<IComponentDefinition> wireUpReaders() {
        final ComponentDefinitionReader componentReader = new ComponentDefinitionReader();
        final ArrayDefinitionReader arrayReader = new ArrayDefinitionReader();
        final CollectionDefinitionReader listReader = new CollectionDefinitionReader(ArrayList.class);
        final CollectionDefinitionReader setReader = new CollectionDefinitionReader(HashSet.class);
        final MapDefinitionReader mapReader = new MapDefinitionReader(HashMap.class);

        final IDefinitionReader<IComponentDefinition> instanceReader;
        instanceReader = new MappingDefinitionReader<IComponentDefinition>(
                ImmutableMap.<String, IDefinitionReader<IComponentDefinition>> of(
                        ConfigTags.COMPONENT.toString(), componentReader,
                        ConfigTags.ARRAY.toString(), arrayReader,
                        ConfigTags.LIST.toString(), listReader,
                        ConfigTags.SET.toString(), setReader,
                        ConfigTags.MAP.toString(), mapReader));

        final IDefinitionReader<IArgumentDefinition> valueReader = new ValueDefinitionReader();
        final IDefinitionReader<IArgumentDefinition> referenceReader = new ReferenceDefinitionReader();
        final IDefinitionReader<IArgumentDefinition> argumentReader = new MappingDefinitionReader<IArgumentDefinition>(
                ImmutableMap.of(
                        ConfigTags.VALUE.toString(), valueReader,
                        ConfigTags.REFERENCE.toString(), referenceReader));

        componentReader.setArgumentReader(argumentReader);
        arrayReader.setArgumentReader(argumentReader);
        listReader.setArgumentReader(argumentReader);
        setReader.setArgumentReader(argumentReader);
        mapReader.setArgumentReader(argumentReader);

        componentReader.setInstanceReader(instanceReader);
        arrayReader.setInstanceReader(instanceReader);
        listReader.setInstanceReader(instanceReader);
        setReader.setInstanceReader(instanceReader);
        mapReader.setInstanceReader(instanceReader);

        return instanceReader;
    }

    /**
     * Returns a list of definitions for all elements under the root of the provided document.
     *
     * @param document to document to be read
     * @return a list of definitions
     * @throws ConfigurationException if an error happens when reading
     */
    public List<IComponentDefinition> readDocument(final Document document) throws ConfigurationException {
        final Element root = document.getRootElement();
        final List<Element> children = ConfigUtils.getAllChildren(root);
        final List<IComponentDefinition> definitions = newArrayListWithCapacity(children.size());
        for(Element element : children) {
            definitions.add(reader.read(element));
        }
        return definitions;
    }
}
