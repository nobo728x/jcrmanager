package com.sptci.jcr.webui.listener.menu;

import com.sptci.jcr.webui.RepositoryDetailsView;

import nextapp.echo.app.event.ActionEvent;

/**
 * The event listener for displaying the {@link
 * com.sptci.jcr.webui.RepositoryDetailsView} in the application.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-25
 * @version $Id: RepositoryDetailsListener.java 22 2009-08-26 14:42:03Z spt $
 */
public class RepositoryDetailsListener extends AbstractSessionListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    if ( checkView() )
    {
      getApplication().addPane( new RepositoryDetailsView() );
    }
    else
    {
      displayError();
    }
  }
}
