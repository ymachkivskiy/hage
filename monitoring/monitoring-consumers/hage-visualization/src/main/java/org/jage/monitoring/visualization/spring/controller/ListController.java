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
package org.jage.monitoring.visualization.spring.controller;


import org.jage.monitoring.MonitoringException;
import org.jage.monitoring.visualization.spring.controller.commandobject.StatisticCommandObject;
import org.jage.monitoring.visualization.storage.StorageDescription;
import org.jage.monitoring.visualization.storage.VisualDataStorageFactory;
import org.jage.monitoring.visualization.storage.element.DescriptionElement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;


/**
 * Controller responsible for handling <code>list</code> command.
 *
 * @author AGH AgE Team
 */
@SuppressWarnings("rawtypes")
@Controller
public class ListController {

    @RequestMapping(value = "/list/{computationType}/{computationInstance}/{gathererId}", method = RequestMethod.GET)
    public String listData(@PathVariable String computationType, @PathVariable String computationInstance, @PathVariable String gathererId, @ModelAttribute("checkedStatistics") StatisticCommandObject statisticCommandObject, ModelMap model) {
        StorageDescription storageDescription = new StorageDescription(computationType, computationInstance, gathererId);
        if(computationInstance.equals("*")) {
            Collection<? extends DescriptionElement> computationDesc;
            try {
                computationDesc = VisualDataStorageFactory.getComputationInstanceCollection(storageDescription);
            } catch(MonitoringException e) {
                return "404";
            }
            List<DescriptionElement> computationDescList = newArrayList(computationDesc);
            Collections.reverse(computationDescList);
            model.addAttribute("cdColl", computationDescList);
            model.addAttribute("computationType", computationType);
            model.addAttribute("gathererId", gathererId);
            return "list/listInstancesSpecial";
        } else {
            Collection<? extends DescriptionElement> computationDesc;
            try {
                computationDesc = VisualDataStorageFactory.getComputationElementCollection(storageDescription);
            } catch(MonitoringException e) {
                return "404";
            }
            List<DescriptionElement> computationDescList = newArrayList(computationDesc);
            model.addAttribute("cdColl", computationDescList);
            return "list/listValues";
        }
    }

    @RequestMapping(value = "/list/{computationType}/{computationInstance}", method = RequestMethod.GET)
    public String listAllGathererId(@PathVariable String computationType, @PathVariable String computationInstance, @ModelAttribute("checkedStatistics") StatisticCommandObject statisticCommandObject, ModelMap model) {
        StorageDescription storageDescription = new StorageDescription(computationType, computationInstance, null);
        Collection<? extends DescriptionElement> computationDesc;
        try {
            computationDesc = VisualDataStorageFactory.getComputationElementCollection(storageDescription);
        } catch(MonitoringException e) {
            return "404";
        }
        model.addAttribute("computationType", computationType);
        model.addAttribute("computationInstance", computationInstance);
        model.addAttribute("cdColl", computationDesc);
        return "list/listGatherers";
    }

    @RequestMapping(value = "/list/{computationType}", method = RequestMethod.GET)
    public String listAllComputationInstance(@PathVariable String computationType, ModelMap model) {
        StorageDescription storageDescription = new StorageDescription(computationType, null, null);
        Collection<? extends DescriptionElement> computationDesc;
        try {
            computationDesc = VisualDataStorageFactory.getComputationElementCollection(storageDescription);
        } catch(MonitoringException e) {
            return "404";
        }
        List<DescriptionElement> computationDescList = newArrayList(computationDesc);
        Collections.reverse(computationDescList);
        model.addAttribute("cdColl", computationDescList);
        return "list/listInstances";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listAllComputationType(ModelMap model) {
        Collection<? extends DescriptionElement> computationDesc;
        try {
            computationDesc = VisualDataStorageFactory.getComputationElementCollection(new StorageDescription());
        } catch(MonitoringException e) {
            return "404";
        }
        model.addAttribute("cdColl", computationDesc);
        return "list/listTypes";
    }

}