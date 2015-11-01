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

package org.jage.monitoring.visualization.rest;


import org.jage.monitoring.visualization.storage.StorageDescription;
import org.jage.monitoring.visualization.storage.VisualData;
import org.jage.monitoring.visualization.storage.VisualDataStorageFactory;
import org.jage.monitoring.visualization.storage.element.VisualDataStorage;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Service class which supports RESTfull requests.
 *
 * @author AGH AgE Team
 */
@Path("/")
public class DataObserverService {


    public DataObserverService() {
    }

    @GET
    @Path("/init")
    public Response initProcessor() {
        VisualDataStorageFactory.loadSavedStorages();
        return Response.status(201).entity("Baza zaladowana").build();
    }

    @POST
    @Path("/{computationType}/{computationInstance}/{gathererId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consumeVisualData(
            @PathParam("computationType") String computationType,
            @PathParam("computationInstance") String computationInstance,
            @PathParam("gathererId") String gathererId,
            VisualData data
    ) {

        StorageDescription storageDescription = new StorageDescription(
                computationType, computationInstance, gathererId);
        String result = "";
        try {
            VisualDataStorage storage = VisualDataStorageFactory.getOrCreateVisualDataStorage(storageDescription);
            storage.save(data);
            result = "Data saved: " + data;

        } catch(Exception e) {
            e.printStackTrace();
        }
        return Response.status(201).entity(result).build();

    }

}
