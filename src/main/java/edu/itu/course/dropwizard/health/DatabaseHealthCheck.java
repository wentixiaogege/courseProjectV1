/*
 * Copyright 2014 Eran C. Withana
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package edu.itu.course.dropwizard.health;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.db.DataSourceFactory;
import org.skife.jdbi.v2.DBI;

// TODO: Auto-generated Javadoc
/**
 * User: Eran Withana
 * Date: 7/7/14.
 */
public class DatabaseHealthCheck extends HealthCheck {
    
    /** The database. */
    private final DBI database;
    
    /** The data source factory. */
    private final DataSourceFactory dataSourceFactory;

    /**
     * Instantiates a new database health check.
     *
     * @param database the database
     * @param dataSourceFactory the data source factory
     */
    public DatabaseHealthCheck(DBI database, DataSourceFactory dataSourceFactory) {
        this.database = database;
        this.dataSourceFactory = dataSourceFactory;
    }

    /* (non Javadoc) 
    * <p>Title: check</p> 
    * <p>Description: </p> 
    * @return
    * @throws Exception 
    * @see com.codahale.metrics.health.HealthCheck#check() 
    */ 
    @Override
    protected Result check() throws Exception {
        if (database.open().getConnection() != null) {
            return Result.healthy();
        } else {
            return Result.unhealthy("Cannot connect to " + dataSourceFactory.getUrl());
        }
    }
}
