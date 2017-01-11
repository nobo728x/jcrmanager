package com.sptci.jcr.webui.listener;

import com.sptci.echo.ErrorPane;
import com.sptci.echo.Listener;
import com.sptci.jcr.SessionManager;
import com.sptci.jcr.webui.WorkspaceCreateDialog;

import nextapp.echo.app.event.ActionEvent;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.Session;

/**
 * The listener for creating a new workspace in the repository.
 * 
 * <p>&copy; Copyright 2010 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-02-12
 * @version $Id: WorkspaceCreateListener.java 63 2010-02-16 15:23:00Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class WorkspaceCreateListener extends Listener<WorkspaceCreateDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final WorkspaceCreateDialog dialog = getView( event );
    if ( checkView( dialog ) )
    {
      Session session = null;

      try
      {
        if ( dialog.isClone() )
        {
          getController().getWorkspace().createWorkspace(
              dialog.getName(), getController().getWorkspace().getName() );
        }
        else
        {
          getController().getWorkspace().createWorkspace( dialog.getName() );
        }

        session = new SessionManager().getSession(
            getController().getCredentials(), dialog.getName() );
        getController().setSession( session );
        dialog.userClose();
      }
      catch ( Throwable e )
      {
        if ( session != null ) session.logout();
        processFatalException( e );
      }
    }
    else
    {
      getApplication().addPane( new ErrorPane(
          getString( dialog, "name.required.title" ),
          getString( dialog, "name.required.message" ) ) );
    }
  }
}
