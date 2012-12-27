
/*
 *  Copyright (c) 2007-2012 Concurrent, Inc. All Rights Reserved.
 *
 *  Project and contact information: http://www.concurrentinc.com/
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.LogLog;
import org.httplog4j.client.HTTPLog4jAppender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class HTTPLog4jAppenderTest
  {
  @Before
  public void setUp()
    {
    LogLog.setInternalDebugging( true );
    Logger root = Logger.getRootLogger();
    root.setLevel( Level.DEBUG );
    root.addAppender( new ConsoleAppender(
      new PatternLayout( PatternLayout.TTCC_CONVERSION_PATTERN ) ) );

    root.addAppender( new HTTPLog4jAppender("http://localhost:8080/logger") );
    }

  @After
  public void tearDown()
    {
    Logger.getRootLogger().removeAllAppenders();
    }

  @Test
  public void basicLogging()
    {
    Logger logger = Logger.getLogger( HTTPLog4jAppenderTest.class );
    logger.info( "a log message" );
    }

  @Test
  public void exceptionLogging()
    {
    try
      {
      throw new Exception( "test exception" );
      }
    catch( Exception exception )
      {
      Logger logger = Logger.getLogger( HTTPLog4jAppenderTest.class );
      logger.error( exception );
      }
    }
  }
