package com.sptci.jcr.webui.listener.menu;

import com.sptci.jcr.webui.RegisterNamespaceDialog;

import nextapp.echo.app.event.ActionEvent;

/**
 * The event listener for displaying the dialogue for registering a custom
 * namespace with the workspace currently in use.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-11-16
 * @version $Id: RegisterNamespaceListener.java 41 2009-11-16 20:16:22Z spt $
 */
public class RegisterNamespaceListener extends AbstractSessionListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    if ( checkView() )
    {
      getApplication().addPane( new RegisterNamespaceDialog() );
    }
    else
    {
      displayError();
    }
  }
}
