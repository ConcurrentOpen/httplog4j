/*
 *  This file is licensed to you under the Apache License, Version 2.0 (the
 *  * "License"); you may not use this file except in compliance
 *  * with the License.  You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package org.httplog4j.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 */
public class HTTPLog4jAppender extends AppenderSkeleton
  {

  private URI remoteURI;
  private HttpClient httpClient;

  public HTTPLog4jAppender( String remoteURI )
    {
    try
      {
      this.remoteURI = new URI( remoteURI );
      }
    catch( URISyntaxException uriSyntaxException )
      {
      errorHandler.error( "could not configure HTTP Log4j Appender, malformed URL provided", uriSyntaxException, ErrorCode.ADDRESS_PARSE_FAILURE );
      LogLog.error( "malformed URL provided on initialization", uriSyntaxException );
      }
    httpClient = new DefaultHttpClient();
    }

  public void append( LoggingEvent event )
    {
    if( event == null )
      return;

    event.getNDC();
    event.getThreadName();
    event.getMDCCopy();
    event.getRenderedMessage();
    event.getThrowableStrRep();

    HttpPost httpPost = new HttpPost( remoteURI );
    try
      {
      httpPost.setEntity( new SerializableEntity( event, true ) );
      HttpResponse response = httpClient.execute( httpPost );

      }
    catch( Exception exception )
      {
      LogLog.warn( "could not publish to logging server", exception );
      }

    }

  public URI getRemoteURI()
    {
    return remoteURI;
    }

  @Override
  public void close()
    {
    httpClient.getConnectionManager().shutdown();
    }

  @Override
  public boolean requiresLayout()
    {
    return false;
    }
  }