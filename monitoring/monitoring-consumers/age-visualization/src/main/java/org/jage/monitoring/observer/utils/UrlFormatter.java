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

package org.jage.monitoring.observer.utils;

import java.util.Iterator;

import com.google.common.base.Splitter;


/**
 * Utility class for formatting string url from monitoring typesafe configuration file. 
 * 
 * @author AGH AgE Team 
 */
public class UrlFormatter {

	public static String removeLastSlash(String url) {
		if (url.length() > 0 && url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		return url;
	}

	public static String getComputationTypeFromUrl(String url){
		url = removeLastSlash(url);
		int compTypeStartIndex = url.lastIndexOf("/")+1;
		return url.substring(compTypeStartIndex, url.length());
	}
	
	public static String getHostFromUrl(String url){
		url = removeLastSlash(url);
		Iterable<String> splited = Splitter.on("/").omitEmptyStrings().split(url);
		Iterator<String> iterator = splited.iterator();
		iterator.next();
		return iterator.next();
	}
	
	public static String getSchemaFromUrl(String url){
		url = removeLastSlash(url);
		Iterable<String> splited = Splitter.on("/").omitEmptyStrings().split(url);
		Iterator<String> iterator = splited.iterator();
		iterator.next();
		iterator.next();
		return iterator.next();
	}
}
