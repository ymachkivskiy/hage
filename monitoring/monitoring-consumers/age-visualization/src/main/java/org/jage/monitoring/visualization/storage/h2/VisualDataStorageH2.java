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
package org.jage.monitoring.visualization.storage.h2;

import static com.google.common.collect.Lists.newLinkedList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.jage.monitoring.visualization.storage.StorageDescription;
import org.jage.monitoring.visualization.storage.VisualData;
import org.jage.monitoring.visualization.storage.element.VisualDataStorage;
import org.jage.monitoring.visualization.storage.h2.config.H2Config;

/**
 *  
 * 
 * @author AGH AgE Team
 *
 */
public class VisualDataStorageH2 implements VisualDataStorage {

	private Date createdDate;
	private String label;
	private StorageDescription storageDescription;
	private String tableName;
	private int id;
	private H2Config h2Config;
	private Connection connection;
	public VisualDataStorageH2(StorageDescription storageDescription) {
		createdDate = new Date();
		h2Config = new H2Config();
		id = 1;
		this.label = storageDescription.toString();
		this.storageDescription = storageDescription;
		StringBuilder sb = new StringBuilder();
		sb.append(storageDescription.getComputationType())
		.append("_")
		.append(storageDescription.getComputationInstance())
		.append("_")
		.append(storageDescription.getGathererId());
		this.tableName = sb.toString();
		try {
	        connection = h2Config.getConnection();
	        Statement stat = connection.createStatement();
	        stat.execute("CREATE TABLE " +
	        		tableName +
	        		" (id INTEGER, value double, timestamp long, PRIMARY KEY (id))");
        } catch (SQLException e) {
	        e.printStackTrace();
        } catch (NamingException e) {
	        e.printStackTrace();
        } finally {
        }
	}
	
	@Override
	public void save(VisualData data) {
		try {
	        PreparedStatement ps = connection.prepareStatement(
	        		"INSERT INTO " +
	        		tableName +
	        		" VALUES(?,?,?)"
	        		);
	        ps.setInt(1, id);
	        ps.setDouble(2, data.getData());
	        ps.setLong(3, data.getTimestamp());
	        ps.execute();
	        id++;
        } catch (SQLException e) {
	        e.printStackTrace();
        }

	}

	
	public Date getCreatedDate() {
		return createdDate;
	}
	public String getLabel() {
		return label;
	}
	public StorageDescription getStorageDescription() {
		return storageDescription;
	}
	public void setStorageDescription(StorageDescription storageDescription) {
		this.storageDescription = storageDescription;
	}

	@Override
    public List<VisualData> getYoungerThan(Date date) {
		List<VisualData> result = newLinkedList();
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT value, timestamp FROM " +
	        		tableName + " WHERE timestamp>?");
			ps.setLong(1, date.getTime());
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()){
	        	result.add(new VisualData(rs.getLong(2), rs.getDouble(1)));
	        }
        } catch (SQLException e) {
	        e.printStackTrace();
        }
		return result;
    }

	@Override
    public Collection<VisualData> all() {
		List<VisualData> result = newLinkedList();
		try {
	        ResultSet rs = connection.createStatement().executeQuery(
	        		"SELECT value, timestamp FROM " +
	        		tableName
	        		);
	        while(rs.next()){
	        	result.add(new VisualData(rs.getLong(2), rs.getDouble(1)));
	        }
        } catch (SQLException e) {
	        e.printStackTrace();
        }
		return result;
    }

	@Override
    public VisualData get(String key) {
	    return null;
    }
}
