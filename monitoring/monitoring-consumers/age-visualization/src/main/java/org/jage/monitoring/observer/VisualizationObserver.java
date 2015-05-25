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
 * File: VisualizationDataForwarder.java
 * Created: 09-03-2013
 * Author: kamilk
 * $Id$
 */

package org.jage.monitoring.observer;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.jage.monitoring.config.ComputationInstanceProvider;
import org.jage.monitoring.config.DefaultComputationInstanceProvider;
import org.jage.monitoring.config.ExecutorProvider;
import org.jage.monitoring.observer.AbstractStatefulObserver;
import org.jage.monitoring.observer.ObservedData;
import org.jage.monitoring.observer.utils.UrlFormatter;
import org.jage.monitoring.visualization.storage.VisualData;
import org.jage.platform.component.provider.IComponentInstanceProvider;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * Data observer which forwards a passed data to Visualization web application.
 * 
 * @author AGH AgE Team
 */
public class VisualizationObserver extends AbstractStatefulObserver {

	private String computationInstance;
	private String url;
	private IComponentInstanceProvider componentInstanceProvider;

	@Inject
	private ComputationInstanceProvider computationInstanceProvider;


	public VisualizationObserver(final String url) {
		this.url = UrlFormatter.removeLastSlash(url);
	}
	
	public VisualizationObserver(String url, IComponentInstanceProvider provider, ExecutorProvider executorProvider){
		this(url);
		this.componentInstanceProvider = provider;
		this.executorProvider = executorProvider;
	}

	@Override
	public void init() {
		super.init();
		if (computationInstanceProvider == null)
			computationInstanceProvider = componentInstanceProvider
					.getInstance(DefaultComputationInstanceProvider.class);
		computationInstance = computationInstanceProvider
				.getComputationInstance().toString();
	}

	@Override
	public void onNext(final ObservedData args) {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				VisualData visualData = new VisualData(args.getTimestamp(), ((Number) args.getData()).doubleValue());
				ClientConfig clientConfig = new DefaultClientConfig();
				Client client = Client.create(clientConfig);
				clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
				WebResource webResource = client.resource(url + "/"	+ computationInstance + "/" + args.getName());
				ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, visualData);
				if (response.getStatus() != 201) {
					throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
				}
			}
		});
	}
}