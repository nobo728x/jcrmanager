package com.sptci.jcr.webui.listener.menu;

import com.sptci.jcr.webui.QueryWindow;

import nextapp.echo.app.event.ActionEvent;

/**
 * The event listener used to display a {@link QueryWindow} in the application.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-11
 * @version $Id: QueryListener.java 10 2009-08-15 23:08:37Z spt $
 */
public class QueryListener extends AbstractSessionListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    if ( checkView() )
    {
      getApplication().addPane( new QueryWindow() );
    }
    else
    {
      displayError();
    }
  }
}
