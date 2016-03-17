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

package org.hage.platform.config.load.xml;


import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.dom4j.Element;
import org.hage.platform.component.container.definition.ConfigurationException;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.getOnlyElement;


/**
 * Static utility methods for configuration processing.
 *
 * @author AGH AgE Team
 */
public final class ConfigUtils {

    private ConfigUtils() {
    }

    public static String getRequiredAttribute(final Element element, final ConfigAttributes attribute) throws ConfigurationException {
        return getRequiredAttribute(element, attribute, false);
    }

    public static String getRequiredAttribute(final Element element, final ConfigAttributes attribute, final boolean allowEmptyString)
            throws ConfigurationException {
        final String attributeValue = element.attributeValue(attribute.toString());
        if(attributeValue == null) {
            throw new ConfigurationException(attribute + " attribute is required");
        }
        if(!allowEmptyString && attributeValue.isEmpty()) {
            throw new ConfigurationException(attribute + " attribute must not be empty");
        }
        return attributeValue;
    }

    public static List<Element> getChildrenIncluding(final Element element, final ConfigTags... tags) {
        final Set<String> includedTags = toSet(tags);
        return copyOf(filter(getAllChildren(element), new Predicate<Element>() {

            @Override
            public boolean apply(final Element element) {
                return includedTags.contains(element.getName());
            }
        }));
    }

    private static Set<String> toSet(final ConfigTags[] tags) {
        final Set<String> set = Sets.newHashSetWithExpectedSize(tags.length);
        for(final ConfigTags tag : tags) {
            set.add(tag.toString());
        }
        return set;
    }

    @SuppressWarnings("unchecked")
    public static List<Element> getAllChildren(final Element element) {
        return element.elements();
    }

    public static List<Element> getChildrenExcluding(final Element element, final ConfigTags... tags) {
        final Set<String> excludedTags = toSet(tags);
        return copyOf(filter(getAllChildren(element), new Predicate<Element>() {

            @Override
            public boolean apply(final Element element) {
                return !excludedTags.contains(element.getName());
            }
        }));
    }

    public static Element getChild(final Element element, final ConfigTags tag) throws ConfigurationException {
        return getOnlyElement(getChildrenIncluding(element, tag));
    }

    @SuppressWarnings("unchecked")
    public static List<Element> getChildrenIncluding(final Element element, final ConfigTags tag) {
        return element.elements(tag.toString());
    }

    public static Element getChild(final Element element) throws ConfigurationException {
        return getOnlyElement(getAllChildren(element));
    }

    public static boolean toBoolean(final String booleanAsString) {
        return Boolean.parseBoolean(booleanAsString);
    }

    public static int toInteger(final String integerAsString) {
        return Integer.parseInt(integerAsString);
    }

    public static Class<?> toClass(final String string) throws ConfigurationException {
        String classAsString = string;
        if(!string.contains(".")) {
            classAsString = "java.lang." + string;
        }
        try {
            return Class.forName(classAsString);
        } catch(final ClassNotFoundException e) {
            throw new ConfigurationException(classAsString + " class couldn't be found");
        }
    }
}
