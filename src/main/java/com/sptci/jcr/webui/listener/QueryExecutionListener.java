package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.AbstractQueryWindow;
import com.sptci.jcr.webui.NodeSearchWindow;
import com.sptci.jcr.webui.QueryResultsView;
import com.sptci.jcr.webui.QueryWindow;

import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;

import static com.sptci.jcr.webui.MainController.getController;
import static java.lang.System.currentTimeMillis;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

/**
 * Event listener for executing JCR queries as entered in {@link
 * com.sptci.jcr.webui.AbstractQueryWindow}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-11
 * @version $Id: QueryExecutionListener.java 63 2010-02-16 15:23:00Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class QueryExecutionListener extends Listener<AbstractQueryWindow>
{
  private static final long serialVersionUID = 1L;
  private long time;

  public void actionPerformed( final ActionEvent event )
  {
    final AbstractQueryWindow window = getView( event );

    if ( checkView( window ) )
    {
      try
      {
        final QueryResult results = execute( window );
        window.setResults( displayResults( results,
            ! ( window instanceof NodeSearchWindow ) ) );

        if ( window instanceof QueryWindow )
        {
          getController().addHistory( window.getQuery() );
        }
      }
      catch ( Throwable t )
      {
        processFatalException( t );
      }
    }
  }

  private QueryResult execute( final AbstractQueryWindow window )
      throws RepositoryException
  {
    final long start = currentTimeMillis();

    final QueryManager qm =
        getController().getWorkspace().getQueryManager();
    Query q = null;

    if ( window instanceof QueryWindow  )
    {
      switch ( ( (QueryWindow) window ).getQueryType() )
      {
        case XPath:
          q = qm.createQuery( window.getQuery(), Query.XPATH );
          break;
        case SQL:
          q = qm.createQuery( window.getQuery(), Query.SQL );
          break;
        case SQL2:
          q = qm.createQuery( window.getQuery(), Query.JCR_SQL2 );
          break;
      }
    }
    else if ( window instanceof NodeSearchWindow )
    {
      q = qm.createQuery(
          String.format( "//*/%s", window.getQuery() ), Query.XPATH );
    }
    else
    {
      q = qm.createQuery( String.format(
          "//*[jcr:contains(.,'%s' )] order by jcr:score() descending",
          window.getQuery() ), Query.XPATH );
    }

    time = currentTimeMillis() - start;
    return q.execute();
  }

  private Component displayResults( final QueryResult results,
      final boolean toolBar )
  {
    return new QueryResultsView( results, time, toolBar );
  }
}
