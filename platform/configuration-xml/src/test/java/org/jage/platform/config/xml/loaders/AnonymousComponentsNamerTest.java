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
 * Created: 2012-04-12
 * $Id$
 */

package org.jage.platform.config.xml.loaders;


import com.google.common.base.Supplier;
import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.ConfigAttributes;
import org.jage.platform.config.xml.ConfigTags;
import org.jage.platform.config.xml.util.DocumentBuilder;
import org.junit.Test;
import org.mockito.Mock;

import static org.jage.platform.config.xml.util.DocumentBuilder.emptyDocument;
import static org.jage.platform.config.xml.util.ElementBuilder.IS_SINGLETON_VALUE;
import static org.jage.platform.config.xml.util.ElementBuilder.SOME_CLASS;
import static org.jage.platform.config.xml.util.ElementBuilder.element;
import static org.mockito.BDDMockito.given;


/**
 * Tests for AnonymousComponentsNamer.
 *
 * @author AGH AgE Team
 */
public class AnonymousComponentsNamerTest extends AbstractDocumentLoaderTest<AnonymousComponentsNamer> {

    @Mock
    private Supplier<String> nameSupplier;

    @Override
    protected AnonymousComponentsNamer getLoader() {
        return new AnonymousComponentsNamer(nameSupplier);
    }

    @Test
    public void shouldNameAnonymousComponentElement() throws ConfigurationException {
        shouldNameAnonymousElement(ConfigTags.COMPONENT);
    }

    private void shouldNameAnonymousElement(final ConfigTags tag) throws ConfigurationException {
        // given
        final String name = "anonymous";
        given(nameSupplier.get()).willReturn(name);

        final DocumentBuilder original = emptyDocument()
                .add(element(tag)
                             .withAttribute(ConfigAttributes.CLASS, SOME_CLASS)
                             .withAttribute(ConfigAttributes.IS_SINGLETON, IS_SINGLETON_VALUE));
        final DocumentBuilder expected = emptyDocument()
                .add(element(tag)
                             .withAttribute(ConfigAttributes.NAME, name)
                             .withAttribute(ConfigAttributes.CLASS, SOME_CLASS)
                             .withAttribute(ConfigAttributes.IS_SINGLETON, IS_SINGLETON_VALUE));

        //then
        assertDocumentTransformation(original, expected);
    }

    @Test
    public void shouldNameAnonymousAgentElement() throws ConfigurationException {
        shouldNameAnonymousElement(ConfigTags.AGENT);
    }

    @Test
    public void shouldNameAnonymousStrategyElement() throws ConfigurationException {
        shouldNameAnonymousElement(ConfigTags.STRATEGY);
    }

    @Test
    public void shouldNameAnonymousArrayElement() throws ConfigurationException {
        shouldNameAnonymousElement(ConfigTags.ARRAY);
    }

    @Test
    public void shouldNameAnonymousListElement() throws ConfigurationException {
        shouldNameAnonymousElement(ConfigTags.LIST);
    }

    @Test
    public void shouldNameAnonymousSetElement() throws ConfigurationException {
        shouldNameAnonymousElement(ConfigTags.SET);
    }

    @Test
    public void shouldNameAnonymousMapElement() throws ConfigurationException {
        shouldNameAnonymousElement(ConfigTags.MAP);
    }
}
