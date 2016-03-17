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

package org.hage.platform.config.load.xml.loaders;


import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.config.load.ConfigurationNotFoundException;
import org.hage.platform.config.load.xml.ConfigAttributes;
import org.hage.platform.config.load.xml.ConfigTags;
import org.hage.platform.config.load.xml.util.DocumentBuilder;
import org.hage.platform.config.load.xml.util.ElementBuilder;
import org.junit.Test;


/**
 * Unit tests for ComponentsNormalizer.
 *
 * @author AGH AgE Team
 */
public class ComponentsNormalizerTest extends AbstractDocumentLoaderTest<ComponentsNormalizer> {

    @Override
    protected ComponentsNormalizer getLoader() {
        return new ComponentsNormalizer();
    }

    @Test
    public void shouldReplaceAgentDefinitions() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(ConfigTags.AGENT));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(ConfigTags.COMPONENT)
                             .withAttribute(ConfigAttributes.IS_SINGLETON, "false"));

        // then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldReplaceStrategyDefinitions() throws ConfigurationException, ConfigurationNotFoundException {
        // given
        final DocumentBuilder original = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(ConfigTags.STRATEGY));
        final DocumentBuilder expected = DocumentBuilder.emptyDocument()
                .add(ElementBuilder.element(ConfigTags.COMPONENT)
                             .withAttribute(ConfigAttributes.IS_SINGLETON, "true"));

        // then
        assertDocumentTransformation(original, expected);
    }
}
