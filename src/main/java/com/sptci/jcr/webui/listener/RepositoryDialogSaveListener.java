package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.LoginLocalDialog;
import com.sptci.jcr.webui.RepositoryDetailsSaveDialog;

import nextapp.echo.app.event.ActionEvent;

/**
 * An event listener for displaying the dialogue used to capture the name
 * to assign to a repository connection setting.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-06
 * @version $Id: RepositoryDialogSaveListener.java 63 2010-02-16 15:23:00Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class RepositoryDialogSaveListener extends Listener<LoginLocalDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    getApplication().addPane(
        new RepositoryDetailsSaveDialog( getView( event ) ) );
  }
}
