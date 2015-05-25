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
 * File: HibernateConfiguration.java
 * Created: 08-09-2012
 * Author: kamilk
 * $Id$
 */

package org.jage.monitoring.persistence.config;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.jage.monitoring.persistence.model.ComputationInstance;
import org.jage.monitoring.persistence.model.ComputationType;
import org.jage.monitoring.persistence.model.Data;
import org.jage.monitoring.persistence.model.GathererName;
import org.jage.monitoring.persistence.model.Property;

/**
 * Configuration Hibernate class.
 * 
 * @author AGH AgE Team
 */
public class HibernateConfiguration {
	private Configuration config;

	private static ServiceRegistry serviceRegistry;

	private static SessionFactory factory = null;

	public HibernateConfiguration() {
	}

	public static SessionFactory getFactory() {
		return factory;
	}

	public Session openSession() {
		return factory.openSession();
	}

	public void initHibernateConfiguration(String url, String user,
			String password) {

		if (factory == null) {

			config = new Configuration();
			config.setProperty("hibernate.connection.url", url);
			config.setProperty("hibernate.connection.username", user);
			config.setProperty("hibernate.connection.password", password);
			config.setProperty("hibernate.default_schema", "age");

			config.setProperty("hibernate.connection.driver_class",	"org.postgresql.Driver");
			config.setProperty("hibernate.dialect",	"org.hibernate.dialect.PostgreSQLDialect");
			config.setProperty("hibernate.connection.pool_size", "1");
			// c3p0 config
			config.setProperty("hibernate.connection.provider_class", "org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider");
			
			config.setProperty("hibernate.c3p0.acquire_increment", "1");
			config.setProperty("hibernate.c3p0.min_size", "5");
			config.setProperty("hibernate.c3p0.max_size", "20");
			config.setProperty("hibernate.c3p0.max_statements", "50");
			config.setProperty("hibernate.c3p0.timeout", "300");
			config.setProperty("hibernate.c3p0.idle_test_period", "300");

			config.setProperty("hibernate.show_sql", "false");
			config.setProperty("hibernate.use_sql_comments", "false"); 
			config.setProperty("hibernate.hbm2ddl.auto", "update");
			config.setProperty("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");
			config.addAnnotatedClass(ComputationType.class);
			config.addAnnotatedClass(ComputationInstance.class);
			config.addAnnotatedClass(GathererName.class);
			config.addAnnotatedClass(Property.class);
			config.addAnnotatedClass(Data.class);
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					config.getProperties()).buildServiceRegistry();
			factory = config.buildSessionFactory(serviceRegistry);
		}
	}
}
