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

package org.hage.platform.config.xml.loaders;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.hage.platform.component.definition.ConfigurationException;
import org.hage.platform.config.xml.ConfigNamespaces;
import org.hage.platform.config.xml.ConfigTags;

import java.util.List;

import static com.google.common.collect.Iterables.consumingIterable;
import static java.lang.String.format;
import static org.dom4j.DocumentHelper.createXPath;


/**
 * Document loader which unwraps all block elements. It assumes all inclusions and overrides have already been resolved
 * by a {@link DocumentResolver}, so it should typically be run after it.
 *
 * @author AGH AgE Team
 */
public class BlockUnwrapper extends AbstractDocumentLoader {

    private static final XPath BLOCKS = initXpath(createXPath(format(
            "//%s:%s",
            ConfigNamespaces.DEFAULT.getPrefix(),
            ConfigTags.BLOCK.toString())));

    @Override
    public Document loadDocument(final String path) throws ConfigurationException {
        final Document document = getDelegate().loadDocument(path);
        unwrapAllBlock(document);
        return document;
    }

    @SuppressWarnings("unchecked")
    private void unwrapAllBlock(final Document document) {
        for(Element block : selectNodes(BLOCKS, document)) {
            final Element parent = block.getParent();

            // get the index of the block element in its parent and remove it
            int index = parent.elements().indexOf(block);
            parent.elements().remove(index);

            // move all child elements from the block to the parent, respecting relative and absolute order
            for(Element child : consumingIterable((List<Element>) block.elements())) {
                parent.elements().add(index++, child);
            }
        }
    }
}
