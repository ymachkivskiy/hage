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
 * Created: 26-07-2012
 * $Id$
 */

package org.hage.platform.config.load.xml.loaders;


import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.hage.platform.component.container.definition.ConfigurationException;
import org.hage.platform.config.load.ConfigurationNotFoundException;
import org.hage.platform.config.load.xml.ConfigAttributes;
import org.hage.platform.config.load.xml.ConfigNamespaces;
import org.hage.platform.config.load.xml.ConfigTags;

import static java.lang.String.format;
import static org.dom4j.DocumentHelper.createXPath;
import static org.hage.platform.config.load.xml.ConfigUtils.getChildrenExcluding;
import static org.hage.platform.config.load.xml.ConfigUtils.getChildrenIncluding;


/**
 * This decorator extracts all shorcut arguments from <property>, <constructor-arg> and <entry> tags, replacing them with full
 * children definitions.
 *
 * @author AGH AgE Team
 */
public class ArgumentShortcutExtractor extends AbstractDocumentLoader {

    private static final XPath CONST_PROP = initXpath(createXPath(format(
            "//%1$s:%2$s | //%1$s:%3$s",
            ConfigNamespaces.DEFAULT.getPrefix(),
            ConfigTags.CONSTRUCTOR_ARG.toString(),
            ConfigTags.PROPERTY.toString())));

    private static final XPath ENTRIES = initXpath(createXPath(format(
            "//%1$s:%2$s",
            ConfigNamespaces.DEFAULT.getPrefix(),
            ConfigTags.ENTRY.toString())));

    @Override
    public Document loadDocument(final String path) throws ConfigurationException, ConfigurationNotFoundException {
        final Document document = getDelegate().loadDocument(path);
        extractConstrPropShortcuts(document);
        extractEntryShortcuts(document);
        return document;
    }

    private void extractConstrPropShortcuts(final Document document) throws ConfigurationException {
        for(final Element element : selectNodes(CONST_PROP, document)) {
            final Attribute valueAttr = element.attribute(ConfigAttributes.VALUE.toString());
            final Attribute typeAttr = element.attribute(ConfigAttributes.TYPE.toString());
            final Attribute refAttr = element.attribute(ConfigAttributes.REF.toString());

            if(bothNotNull(valueAttr, refAttr)) {
                throw new ConfigurationException(element.getUniquePath() + ": Value and ref shortcut attributes can't be both set");
            }

            if(anyNotNull(valueAttr, refAttr)) {
                if(!element.elements().isEmpty()) {
                    throw new ConfigurationException(element.getUniquePath()
                                                             + ": there can't be both a shortcut attribute and a child definition of some argument");
                }

                if(valueAttr != null) {
                    final String value = valueAttr.getValue();
                    final String type = typeAttr.getValue();
                    element.remove(typeAttr);
                    element.remove(valueAttr);
                    element.add(newValueElement(type, value));
                } else if(refAttr != null) {
                    final String ref = refAttr.getValue();
                    element.remove(refAttr);
                    element.add(newReferenceElement(ref));
                }
            } else if(element.elements().isEmpty()) {
                throw new ConfigurationException(element.getUniquePath()
                                                         + ": there must be either a shortcut attribute or a child definition of some argument");
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void extractEntryShortcuts(final Document document) throws ConfigurationException {
        for(final Element element : selectNodes(ENTRIES, document)) {
            final Attribute keyAttr = element.attribute(ConfigAttributes.KEY.toString());
            final Attribute keyRefAttr = element.attribute(ConfigAttributes.KEY_REF.toString());

            if(bothNotNull(keyAttr, keyRefAttr)) {
                throw new ConfigurationException(element.getUniquePath() + ": key and key-ref shortcut attributes can't be both set");
            }

            if(anyNotNull(keyAttr, keyRefAttr)) {
                if(!getChildrenIncluding(element, ConfigTags.KEY).isEmpty()) {
                    throw new ConfigurationException(element.getUniquePath()
                                                             + ": there can't be both a key shortcut attribute and a child definition of some argument");
                }

                if(keyAttr != null) {
                    final String key = keyAttr.getValue();
                    element.remove(keyAttr);
                    element.elements().add(0, newKeyElement(newValueElement(key)));
                } else if(keyRefAttr != null) {
                    final String keyRef = keyRefAttr.getValue();
                    element.remove(keyRefAttr);
                    element.elements().add(0, newKeyElement(newReferenceElement(keyRef)));
                }
            } else if(getChildrenIncluding(element, ConfigTags.KEY).isEmpty()) {
                throw new ConfigurationException(element.getUniquePath()
                                                         + ": there must be either a key shortcut attribute or a child definition of some argument");
            }

            final Attribute valueAttr = element.attribute(ConfigAttributes.VALUE.toString());
            final Attribute valueRefAttr = element.attribute(ConfigAttributes.VALUE_REF.toString());

            if(bothNotNull(valueAttr, valueRefAttr)) {
                throw new ConfigurationException(element.getUniquePath() + ": value and value-ref shortcut attributes can't be both set");
            }

            if(anyNotNull(valueAttr, valueRefAttr)) {
                if(!getChildrenExcluding(element, ConfigTags.KEY).isEmpty()) {
                    throw new ConfigurationException(element.getUniquePath()
                                                             + ": there can't be both a value shortcut attribute and a child definition of some argument");
                }

                if(valueAttr != null) {
                    final String value = valueAttr.getValue();
                    element.remove(valueAttr);
                    element.add(newValueElement(value));
                } else if(valueRefAttr != null) {
                    final String valueRef = valueRefAttr.getValue();
                    element.remove(valueRefAttr);
                    element.add(newReferenceElement(valueRef));
                }
            } else if(getChildrenExcluding(element, ConfigTags.KEY).isEmpty()) {
                throw new ConfigurationException(element.getUniquePath()
                                                         + ": there must be either a value shortcut attribute or a child definition of some argument");
            }
        }
    }

    private boolean bothNotNull(final Object o1, final Object o2) {
        return o1 != null && o2 != null;
    }

    private boolean anyNotNull(final Object o1, final Object o2) {
        return o1 != null || o2 != null;
    }
}
