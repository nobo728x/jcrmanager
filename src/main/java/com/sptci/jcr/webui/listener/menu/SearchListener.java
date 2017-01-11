package com.sptci.jcr.webui.listener.menu;

import com.sptci.jcr.webui.SearchWindow;

import nextapp.echo.app.event.ActionEvent;

/**
 * The event listener for displaying the repository search window.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-12
 * @version $Id: SearchListener.java 10 2009-08-15 23:08:37Z spt $
 */
public class SearchListener extends AbstractSessionListener
{
  private static final long serialVersionUID = 1l;

  public void actionPerformed( final ActionEvent event )
  {
    if ( checkView() )
    {
      getApplication().addPane( new SearchWindow() );
    }
    else
    {
      displayError();
    }
  }
}
