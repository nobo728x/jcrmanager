package com.sptci.jcr.webui.listener.menu;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.ContentArea;
import com.sptci.jcr.webui.LoginRMIDialog;

import nextapp.echo.app.event.ActionEvent;

/**
 * The action listener for displaying the dialog that will be used to
 * enter the RMI URL and login credentials to access a remote repository.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-02-15
 * @version $Id: LoginRMIListener.java 63 2010-02-16 15:23:00Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class LoginRMIListener extends Listener<ContentArea>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    getApplication().addPane( new LoginRMIDialog() );
  }
}