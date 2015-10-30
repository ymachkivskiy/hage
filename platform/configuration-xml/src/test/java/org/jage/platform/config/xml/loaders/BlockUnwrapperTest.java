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
 * Created: 2012-04-15
 * $Id$
 */

package org.jage.platform.config.xml.loaders;


import org.jage.platform.component.definition.ConfigurationException;
import org.jage.platform.config.xml.util.DocumentBuilder;
import org.junit.Test;

import static org.jage.platform.config.xml.util.DocumentBuilder.emptyDocument;
import static org.jage.platform.config.xml.util.ElementBuilder.*;


/**
 * Tests for BlockUnwrapper.
 *
 * @author AGH AgE Team
 */
public class BlockUnwrapperTest extends AbstractDocumentLoaderTest<BlockUnwrapper> {

    @Override
    protected BlockUnwrapper getLoader() {
        return new BlockUnwrapper();
    }

    @Test
    public void shouldUnwrapAllBlocks() throws ConfigurationException {
        // given
        final DocumentBuilder original = emptyDocument()
                .add(blockElement(SOME_NAME)
                             .withBody(
                                     componentElement(),
                                     listElement(),
                                     blockElement(SOME_NAME)
                                             .withBody(arrayElement())))
                .add(componentElement()
                             .withBody(
                                     blockElement(SOME_NAME)
                                             .withBody(mapElement())));
        final DocumentBuilder expected = emptyDocument()
                .add(componentElement())
                .add(listElement())
                .add(arrayElement())
                .add(componentElement()
                             .withBody(mapElement()));

        // then
        assertDocumentTransformation(original, expected);
    }
}
