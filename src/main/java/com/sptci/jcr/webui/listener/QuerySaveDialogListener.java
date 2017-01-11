package com.sptci.jcr.webui.listener;

import static com.sptci.echo.Configuration.getString;
import com.sptci.echo.ErrorPane;
import com.sptci.echo.Listener;
import com.sptci.jcr.webui.QuerySaveDialog;
import com.sptci.jcr.webui.QueryWindow;

import nextapp.echo.app.event.ActionEvent;

/**
 * The event listener for saving the query displayed in {@link QueryWindow}
 * Displays a dialogue for capturing the name to assign to the query.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-10-01
 * @version $Id: QuerySaveDialogListener.java 35 2009-10-01 20:01:23Z spt $
 */
public class QuerySaveDialogListener extends Listener<QueryWindow>
{
  private static final long serialVersionUID = 1l;

  public void actionPerformed( final ActionEvent event )
  {
    final QueryWindow window = getView( event );

    if ( checkView( window ) )
    {
      getApplication().addPane( new QuerySaveDialog( window.getQuery() ) );
    }
    else
    {
      getApplication().addPane( new ErrorPane(
          getString( window, "error.title" ),
          getString( window, "error.message" ) ) );
    }
  }
}
