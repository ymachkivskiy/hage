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


import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jage.monitoring.MonitoringException;
import org.jage.monitoring.visualization.spring.controller.commandobject.StatisticCommandObject;
import org.jage.monitoring.visualization.storage.DataSupplierService;
import org.jage.monitoring.visualization.storage.DataSupplierServiceImpl;
import org.jage.monitoring.visualization.storage.StorageChooserService;
import org.jage.monitoring.visualization.storage.StorageChooserServiceImpl;
import org.jage.monitoring.visualization.storage.StorageDescription;
import org.jage.monitoring.visualization.storage.VisualData;
import org.jage.monitoring.visualization.storage.element.VisualDataStorage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Controller responsible for handling <code>show</code> command.
 *
 * @author AGH AgE Team
 */
@Controller
public class ShowController {

    private StorageChooserService storageChooserService = new StorageChooserServiceImpl();
    private DataSupplierService dataSupplierService = new DataSupplierServiceImpl();

    @RequestMapping(value = "/showChecked", method = RequestMethod.POST)
    public String showChecked(@ModelAttribute("checkedStatistics") StatisticCommandObject values,
            @RequestParam("computationType") String computationType,
            @RequestParam("computationInstance") String computationInstance,
            @RequestParam("gathererId") String gathererId,
            ModelMap model) {

        Collection<Integer> checkboxNumber = values.getCheckboxNumber();
        if(checkboxNumber == null) {
            return "404";
        }
        StringBuilder statsStringBuilder = new StringBuilder();
        for(Integer i : checkboxNumber) {
            statsStringBuilder.append(i.toString()).append(",");
        }
        statsStringBuilder.deleteCharAt(statsStringBuilder.lastIndexOf(","));

        StringBuilder redirectStringBuilder = new StringBuilder("redirect:show/");
        redirectStringBuilder.append(computationType).append("/").append(computationInstance);
        if(!gathererId.equals("")) {
            redirectStringBuilder.append("/").append(gathererId);
        }
        redirectStringBuilder.append("?stats=").append(statsStringBuilder.toString());

        return redirectStringBuilder.toString();
    }

    @RequestMapping("/show/{computationType}/{computationInstance}/{gathererId}")
    public String runVisualData(
            @PathVariable("computationType") String computationType,
            @PathVariable("computationInstance") String computationInstance,
            @PathVariable("gathererId") String gathererId,
            @RequestParam(value = "stats", required = false) String stats,
            ModelMap model
    ) throws URISyntaxException {

        try {
            model = createParametersMap(model, computationType, computationInstance, gathererId, stats);
        } catch(MonitoringException e) {
            return "404";
        }
        return "show/show";
    }

    private ModelMap createParametersMap(ModelMap map, String computationType, String computationInstance, String gathererId, String stats) {
        if(computationType != null) {
            map.put("computationType", computationType);
        }
        if(computationInstance != null) {
            map.put("computationInstance", computationInstance);
        }
        if(gathererId != null) {
            map.put("gathererId", gathererId);
        }
        if(stats != null) {
            map.put("stats", stats);
        }
        String chartId = new Long(new Date().getTime()).toString();
        List<HighchartsSeries> seriesList = createHighchartsSeriesList(computationType, computationInstance, gathererId, stats, chartId);
        ObjectMapper mapper = new ObjectMapper();
        String seriesListJson = "";
        try {
            seriesListJson = mapper.writeValueAsString(seriesList);
        } catch(JsonGenerationException e) {
            e.printStackTrace();
        } catch(JsonMappingException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        map.put("chartId", chartId);
        map.put("seriesList", seriesListJson);
        return map;
    }

    private List<HighchartsSeries> createHighchartsSeriesList(String computationType, String computationInstance, String gathererId,
            String stats, String chartId) {
        Collection<VisualDataStorage> storagesList = null;
        if(computationType != null && computationInstance != null && gathererId != null && computationInstance.equals("*")) {
            storagesList = storageChooserService.getAndSaveVisualDataStorageCollectionByWildcard(new StorageDescription(computationType, computationInstance, gathererId), chartId, stats);
        } else if(computationType != null && computationInstance != null && gathererId != null) {
            storagesList = new LinkedList<VisualDataStorage>();
            storagesList.add(storageChooserService.getAndSaveVisualDataStorage(new StorageDescription(computationType, computationInstance, gathererId), chartId));
        } else {
            storagesList = storageChooserService.getAndSaveVisualDataStorageCollection(new StorageDescription(computationType, computationInstance, gathererId), chartId, stats);
        }
        List<HighchartsSeries> seriesList = new LinkedList<HighchartsSeries>();
        for(VisualDataStorage storage : storagesList) {
            if(storage == null) {
                throw new MonitoringException("No resource");
            }
            Object[] o = new Object[1];
            o[0] = null;
            seriesList.add(new HighchartsSeries(storage.getStorageDescription().getGathererId(), o));
        }
        return seriesList;
    }

    @RequestMapping("/show/{computationType}/{computationInstance}")
    public String runVisualData(
            @PathVariable("computationType") String computationType,
            @PathVariable("computationInstance") String computationInstance,
            @RequestParam(value = "stats", required = false) String stats,
            ModelMap model
    ) throws URISyntaxException {
        try {
            model = createParametersMap(model, computationType, computationInstance, null, stats);
        } catch(MonitoringException e) {
            return "404";
        }
        return "show/show";
    }

    @RequestMapping("/show/{computationType}")
    public String runVisualData(
            @PathVariable("computationType") String computationType,
            @RequestParam(value = "stats", required = false) String stats,
            ModelMap model
    ) throws URISyntaxException {
        return "404";
    }

    @RequestMapping("/update")
    public
    @ResponseBody
    List<LinkedList<VisualData>> updateData(@RequestParam String chartId, @RequestParam Map<String, String> lastDateMap) {

        Map<Integer, Date> lastDateMapResult = new LinkedHashMap<Integer, Date>();
        Set<Entry<String, String>> entrySet = lastDateMap.entrySet();
        Pattern p = Pattern.compile(
                "lastDateMap\\[(.*)\\]",
                Pattern.DOTALL
        );
        for(Entry<String, String> entry : entrySet) {
            Matcher matcher = p.matcher(
                    entry.getKey()
            );

            if(matcher.matches()) {
                lastDateMapResult.put(new Integer(matcher.group(1)), new Date(new Long(entry.getValue())));
            }
        }
        List<LinkedList<VisualData>> list = dataSupplierService.get(lastDateMapResult, chartId);
        return list;
    }
}
