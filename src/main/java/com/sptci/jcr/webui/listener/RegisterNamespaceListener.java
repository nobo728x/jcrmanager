package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.RegisterNamespaceDialog;

import nextapp.echo.app.event.ActionEvent;

import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.RepositoryException;

/**
 * The event listener for registering the namespace as entered in {@link
 * com.sptci.jcr.webui.RegisterNamespaceDialog}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-11-16
 * @version $Id: RegisterNamespaceListener.java 63 2010-02-16 15:23:00Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class RegisterNamespaceListener extends Listener<RegisterNamespaceDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final RegisterNamespaceDialog dialog = getView( event );

    try
    {
      getController().getWorkspace().getNamespaceRegistry().
          registerNamespace( dialog.getPrefix(), dialog.getURI() );

      dialog.userClose();
    }
    catch ( RepositoryException e )
    {
      processFatalException( e );
    }
  }
}
