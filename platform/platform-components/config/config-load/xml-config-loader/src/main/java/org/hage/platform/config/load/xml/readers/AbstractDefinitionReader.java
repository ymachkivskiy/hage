package org.hage.platform.config.load.xml.readers;


import org.hage.platform.component.definition.IArgumentDefinition;
import org.hage.platform.component.definition.IComponentDefinition;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Abstract base class for DefinitionReaders. Allow to set nested instance and arguments readers, if needed.
 *
 * @author AGH AgE Team
 */
public abstract class AbstractDefinitionReader<T> implements IDefinitionReader<IComponentDefinition> {

    private IDefinitionReader<IArgumentDefinition> argumentReader;

    private IDefinitionReader<IComponentDefinition> instanceReader;

    /**
     * Get the nested argument definition reader, if available.
     *
     * @return a nested argument definition reader or null
     */
    public final IDefinitionReader<IArgumentDefinition> getArgumentReader() {
        return argumentReader;
    }

    /**
     * Set the nested argument definition reader.
     *
     * @param argumentReader a nested argument definition reader
     */
    public final void setArgumentReader(final IDefinitionReader<IArgumentDefinition> argumentReader) {
        this.argumentReader = checkNotNull(argumentReader);
    }

    /**
     * Get the nested instance definition reader, if available.
     *
     * @return a nested instance definition reader or null
     */
    public final IDefinitionReader<IComponentDefinition> getInstanceReader() {
        return instanceReader;
    }

    /**
     * Set the nested instance definition reader.
     *
     * @param argumentReader a nested instance definition reader
     */
    public final void setInstanceReader(final IDefinitionReader<IComponentDefinition> instanceReader) {
        this.instanceReader = checkNotNull(instanceReader);
    }
}
