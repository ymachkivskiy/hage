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
package org.jage.monitoring.graphite;

import static java.lang.String.format;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.jage.monitoring.observer.ObservedData;
import org.jage.platform.component.IStatefulComponent;
import org.jage.platform.component.exception.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observer;

public class GraphiteObserver implements Observer<ObservedData>, IStatefulComponent {

	private static final Logger log = LoggerFactory.getLogger(GraphiteObserver.class);

	private final String host;

	private final int port;

	private Socket socket;

	private PrintWriter writer;

	public GraphiteObserver() {
		this("localhost", 2003);
    }

	public GraphiteObserver(final String host, final int port) {
	    this.host = host;
	    this.port = port;
    }

	@Override
	public void init() throws ComponentException {
		try {
			socket = new Socket(host, port);
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			throw new ComponentException(format("Could not connect to carbon at %s:%d - {}", host, port), e);
		}
	}

	@Override
	public void onNext(ObservedData data) {
		if (writer == null) {
			log.warn("Pushing data to unconnected graphite consumer. Input ignored - the component has to be initialized first");
		} else {
			writer.println(format("%s %s %d", data.getName(), data.getData().toString(), data.getTimestamp()/1000));
			writer.flush();
		}
	}

	@Override
	public boolean finish() throws ComponentException {
		try {
			writer.close();
			socket.close();
		} catch (IOException e) {
			log.error("Error closing network socket", e);
		}
		return false;
	}

	@Override
	public void onCompleted() {}

	@Override
	public void onError(Throwable e) {}
}
