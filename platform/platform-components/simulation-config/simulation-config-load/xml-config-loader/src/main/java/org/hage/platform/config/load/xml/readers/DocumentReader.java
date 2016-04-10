package org.hage.platform.simulationconfig.load.xml.readers;


import com.google.common.collect.ImmutableMap;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.component.container.definition.IArgumentDefinition;
import org.hage.platform.component.container.definition.IComponentDefinition;
import org.hage.platform.simulationconfig.load.xml.ConfigTags;
import org.hage.platform.simulationconfig.load.xml.ConfigUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.google.common.collect.Lists.newArrayListWithCapacity;



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
