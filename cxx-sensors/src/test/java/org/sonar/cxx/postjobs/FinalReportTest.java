/*
 * Sonar C++ Plugin (Community)
 * Copyright (C) 2010-2020 SonarOpenCommunity
 * http://github.com/SonarOpenCommunity/sonar-cxx
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.cxx.postjobs;

import java.io.File;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.postjob.PostJobContext;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;
import org.sonar.cxx.CxxAstScanner;
import org.sonar.cxx.preprocessor.CxxPreprocessor;
import org.sonar.cxx.visitors.CxxParseErrorLoggerVisitor;

public class FinalReportTest {

  @org.junit.Rule
  public LogTester logTester = new LogTester();
  private PostJobContext postJobContext;

  @Before
  public void scanFile() {
    postJobContext = Mockito.mock(PostJobContext.class);
  }

  @Test
  public void finalReportTest() {
    var dir = "src/test/resources/org/sonar/cxx/postjobs";
    var context = SensorContextTester.create(new File(dir));
    InputFile inputFile = TestInputFileBuilder.create("", dir + "/syntaxerror.cc").build();
    context.fileSystem().add(inputFile);

    CxxParseErrorLoggerVisitor.resetReport();
    CxxPreprocessor.resetReport();
    CxxAstScanner.scanSingleFile(new File(inputFile.uri().getPath()));

    var postjob = new FinalReport();
    postjob.execute(postJobContext);

    var log = logTester.logs(LoggerLevel.WARN);
    assertThat(log.size()).isEqualTo(2);
    assertThat(log.get(0)).contains("include directive error(s)");
    assertThat(log.get(1)).contains("syntax error(s) detected");
  }

}
