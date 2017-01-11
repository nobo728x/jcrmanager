package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.QueryHistoryWindow;
import com.sptci.jcr.webui.QueryWindow;

import nextapp.echo.app.event.ActionEvent;

/**
 * The event listener for displaying the history of queries executed from
 * the current {@link com.sptci.jcr.webui.QueryWindow}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-07
 * @version $Id$
 */
public class QueryHistoryListener extends Listener<QueryWindow>
{
  private static final long serialVersionUID = 1l;

  public void actionPerformed( final ActionEvent event )
  {
    final QueryWindow window = getView( event );
    getApplication().addPane( new QueryHistoryWindow( window ) );
  }
}
