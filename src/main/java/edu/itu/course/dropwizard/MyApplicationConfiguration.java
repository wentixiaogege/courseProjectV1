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

package edu.itu.course.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xeiam.dropwizard.sundial.SundialConfiguration;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

// TODO: Auto-generated Javadoc
/**
 * User: Jack Li
 * Date: 7/7/15.
 */
public class MyApplicationConfiguration extends Configuration{

    /** The database. */
    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    /**
     * Gets the data source factory.
     *
     * @return the data source factory
     */
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

	
  /** The sundial configuration. */
  // Sundial
  @Valid
  @NotNull
  public SundialConfiguration sundialConfiguration = new SundialConfiguration();

  /**
   * Gets the sundial configuration.
   *
   * @return the sundial configuration
   */
  @JsonProperty("sundial")
  public SundialConfiguration getSundialConfiguration() {

    return sundialConfiguration;
  }
}
