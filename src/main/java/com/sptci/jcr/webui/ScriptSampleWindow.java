package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;

import echopoint.DirectHtml;

import static com.sptci.echo.Application.getApplication;
import static echopoint.util.ColorKit.makeColor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A window that displays the sample Groovy script to help users understand
 * how to write powerful scripts and execute them from the application.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-01-12
 * @version $Id: ScriptSampleWindow.java 44 2010-01-12 21:01:38Z spt $
 */
public class ScriptSampleWindow extends WindowPane
{
  private static final long serialVersionUID = 1L;

  public ScriptSampleWindow()
  {
    setBackground( makeColor( "#020A4F") );
  }

  public void init()
  {
    removeAll();
    super.init();

    add( new DirectHtml( getContent() ) );
  }

  private String getContent()
  {
    final StringBuilder builder = new StringBuilder( 1024 );
    final String resource = "/META-INF/resource/CreateTestNodes.html";
    final InputStream is = getClass().getResourceAsStream( resource );
    BufferedReader reader = null;

    try
    {
      reader = new BufferedReader( new InputStreamReader( is ) );
      String line;

      while ( ( line = reader.readLine() ) != null )
      {
        builder.append( line );
      }
    }
    catch ( Throwable t )
    {
      getApplication().processFatalException( t );
    }
    finally
    {
      try
      {
        if ( reader != null ) reader.close();
        is.close();
      }
      catch ( Throwable t )
      {
        getApplication().processFatalException( t );
      }
    }

    return builder.toString();
  }
}
