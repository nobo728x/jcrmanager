package com.sptci.jcr.webui.listener.menu;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.ContentArea;
import com.sptci.jcr.webui.LoginLocalDialog;

import nextapp.echo.app.event.ActionEvent;

/**
 * The action listener for displaying the file dialog that will be used to
 * select the repository configuration file and repository directory.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-06-22
 * @version $Id: LoginLocalListener.java 63 2010-02-16 15:23:00Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class LoginLocalListener extends Listener<ContentArea>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final LoginLocalDialog pane = new LoginLocalDialog();
    getApplication().addPane( pane );
  }
}
