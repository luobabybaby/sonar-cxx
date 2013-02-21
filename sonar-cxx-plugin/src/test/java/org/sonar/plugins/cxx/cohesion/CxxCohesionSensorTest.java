/*
 * Sonar C++ Plugin (Community)
 * Copyright (C) 2010 Neticoa SAS France
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.cxx.cohesion;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.plugins.cxx.TestUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CxxCohesionSensorTest {

  Project project;
  SensorContext context;
  CxxCohesionSensor sensor;

  @Before
  public void setup() throws URISyntaxException {
    File baseDir = TestUtils.loadResource("/org/sonar/plugins/cxx/cohesion");

    List<File> sourceDirs = new ArrayList<File>();
    List<File> testDirs = new ArrayList<File>();
    sourceDirs.add(baseDir);

    context = mock(SensorContext.class);
    Resource<?> resource = mock(Resource.class);
    when(context.getResource(any(Resource.class))).thenReturn(resource);

    project = TestUtils.mockProject(baseDir, sourceDirs, testDirs);

    sensor = new CxxCohesionSensor();
  }

  @Test
  public void shouldExecuteOnProjectTest() {
    assertTrue(sensor.shouldExecuteOnProject(project));
  }

  @Test
  public void analyseTest() {
    sensor.analyse(project, context);

    // TODO: use the following statements to check the SSLR-based implementation of the
    // cohesion sensor, once its done.
    // verify(context).saveMeasure(any(Resource.class), eq(CoreMetrics.LCOM4), eq(0.0)); //NoMethodsClass.cpp
    // verify(context).saveMeasure(any(Resource.class), eq(CoreMetrics.LCOM4), eq(1.0)); //IdealCohesion.cpp
    // verify(context).saveMeasure(any(Resource.class), eq(CoreMetrics.LCOM4), eq(2.0)); //LowCohesion.cpp
    // verify(context).saveMeasure(any(Resource.class), eq(CoreMetrics.LCOM4), eq(3.0)); //Driver.cpp
    // verify(context).saveMeasure(any(Resource.class), eq(CoreMetrics.LCOM4), eq(5.0)); //HighCohesion.cpp
    // verify(context, times(5)).saveMeasure(any(Resource.class), (Measure)anyObject());
  }

}
