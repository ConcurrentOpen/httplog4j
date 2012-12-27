
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

package org.httplog4j.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 */
public class Log4jServlet extends HttpServlet
  {
  private static final Logger logger = Logger.getLogger( Log4jServlet.class );
  private LoggerRepository hierarchy = LogManager.getLoggerRepository();


  public void init()
    {
    logger.info( "initializing Log4j Servlet" );
    }

  protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws IOException
    {
    ObjectInputStream ois = new ObjectInputStream( req.getInputStream() );
    try
      {
      LoggingEvent event = (LoggingEvent) ois.readObject();
      // get a logger from the hierarchy. The name of the logger is taken to be the name contained in the event.
      Logger remoteLogger = hierarchy.getLogger( event.getLoggerName() );
      // apply the logger-level filter
      if( event.getLevel().isGreaterOrEqual( remoteLogger.getEffectiveLevel() ) )
        {
        // finally log the event as if was generated locally
        remoteLogger.callAppenders( event );
        }
      }
    catch( ClassNotFoundException classNotFound )
      {
      logger.error( "received object without a known class, this should never happen", classNotFound );
      }

    }
  }
