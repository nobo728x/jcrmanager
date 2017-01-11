package com.sptci.jcr.webui.listener;

import com.sptci.echo.Application;
import com.sptci.echo.http.ContextListener;
import com.sptci.jcr.SessionManager;
import com.sptci.jcr.webui.model.QueryHistory;

import static java.util.logging.Level.SEVERE;

import javax.jcr.Session;
import javax.servlet.http.HttpSession;

import java.io.FileNotFoundException;

/**
 * A session listener used to logout of the JCR session.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-06-02
 * @version $Id: SessionListener.java 44 2010-01-12 21:01:38Z spt $
 */
public class SessionListener extends ContextListener
{
  @Override
  protected void close( final HttpSession httpSession )
  {
    for ( final Application app : getApplications( httpSession ) )
    {
      final Session session = (Session) app.getProperty( Session.class.getName() );

      if ( session != null )
      {
        logger.info( "Logging out of JCR" );
        session.logout();
      }
      else
      {
        logger.info( "Session is null" );
      }

      saveHistory( app );
    }
  }

  @Override
  protected void close()
  {
    new SessionManager().shutdown();
  }

  protected void saveHistory( final Application app )
  {
    final QueryHistory history = (QueryHistory)
        app.getProperty( QueryHistory.class.getName() );

    if ( history != null )
    {
      try
      {
        history.save();
      }
      catch ( FileNotFoundException e )
      {
        logger.log( SEVERE, "Error serialising query history", e );
      }
    }
  }
}
