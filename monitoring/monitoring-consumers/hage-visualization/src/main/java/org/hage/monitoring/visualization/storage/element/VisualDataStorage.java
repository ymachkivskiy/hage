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
package org.hage.monitoring.visualization.storage.element;


import org.hage.monitoring.visualization.storage.StorageDescription;
import org.hage.monitoring.visualization.storage.VisualData;

import java.util.Date;
import java.util.List;


/**
 * @author AGH AgE Team
 */
public interface VisualDataStorage extends DescriptionElement<VisualData> {

    /**
     * Saves passed VisualData instance.
     *
     * @param data
     */
    public void save(VisualData data);

    /**
     * Gets VisualData instances which are newer than date which is pointed by passed Date object.
     *
     * @param date
     * @return
     */
    public List<VisualData> getYoungerThan(Date date);

    /**
     * Returns StorageDescription object which describes VisualDataStorage.
     *
     * @return
     */
    public StorageDescription getStorageDescription();
}