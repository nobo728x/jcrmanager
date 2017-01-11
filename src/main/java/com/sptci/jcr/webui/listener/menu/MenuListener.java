package com.sptci.jcr.webui.listener.menu;

import com.sptci.echo.ErrorPane;
import com.sptci.echo.Listener;
import com.sptci.jcr.SessionManager;
import com.sptci.jcr.webui.ContentArea;
import com.sptci.jcr.webui.LoginJNDIDialog;
import com.sptci.jcr.webui.LoginLocalDialog;
import com.sptci.jcr.webui.LoginRMIDialog;
import com.sptci.jcr.webui.QueryWindow;
import com.sptci.jcr.webui.ResourceDialog;
import com.sptci.jcr.webui.ScriptManagementWindow;
import com.sptci.jcr.webui.ScriptSampleWindow;
import com.sptci.jcr.webui.WorkspaceCreateDialog;
import com.sptci.jcr.webui.listener.ScriptEditorExecuteListener;
import com.sptci.jcr.webui.model.RepositoryConnectionData;
import com.sptci.jcr.webui.model.RepositoryData;

import nextapp.echo.app.event.ActionEvent;

import static com.sptci.ReflectionUtility.execute;
import static com.sptci.ReflectionUtility.newInstance;
import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;
import static com.sptci.util.StringUtilities.fromFile;
import static java.lang.String.format;
import static java.util.logging.Level.FINE;

import javax.jcr.Session;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-06-19
 * @version $Id: MenuListener.java 73 2011-11-04 17:23:50Z orair $
 */
public class MenuListener extends Listener<ContentArea>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final String command = event.getActionCommand();

    final String name = format( "%s.%s%s%s",
        MenuListener.class.getPackage().getName(),
        command.substring( 0, 1 ).toUpperCase(),
        command.substring( 1 ), "Listener" );

    try
    {
      final Object listener = newInstance( name );
      execute( listener, "actionPerformed", event );
    }
    catch ( Throwable t )
    {
      logger.log( FINE, format( "No listener class %s%n", name ), t );
      handleAction( event );
    }
  }

  private void handleAction( final ActionEvent event )
  {
    if ( event.getActionCommand().startsWith( "resource:" ) )
    {
      processResource( event );
    }
    else if ( event.getActionCommand().startsWith( "saved:" ) )
    {
      processSaved( event );
    }
    else if ( event.getActionCommand().startsWith( "names:" ) )
    {
      processNamedQuery( event );
    }
    else if ( event.getActionCommand().startsWith( "scripts:" ) )
    {
      processScript( event );
    }
    else if ( event.getActionCommand().startsWith( "workspaces:" ) )
    {
      processWorkspace( event );
    }
  }

  private void processResource( final ActionEvent event )
  {
    final String resource = event.getActionCommand().replace( "resource:", "" );
    getApplication().addPane( new ResourceDialog( resource ) );
  }

  private void processSaved( final ActionEvent event )
  {
    final String name = event.getActionCommand().replace( "saved:", "" );
    final RepositoryConnectionData connection =
        getController().getConnectionData();
    final RepositoryData data = connection.getRepositoryData( name );

    if ( data.getRmi() == null )
    {
      if ( data.getJndi() == null )
      {
        final LoginLocalDialog dialog = new LoginLocalDialog();
        dialog.setFile( data.getConfiguration() );
        dialog.setDirectory( data.getDirectory() );
        dialog.setUser( data.getUserName() );
        dialog.setPassword( data.getPassword() );

        getApplication().addPane( dialog );
      }else
      {
        final LoginJNDIDialog dialog = new LoginJNDIDialog();
        dialog.setJndi( data.getJndi() );
        dialog.setUser( data.getUserName() );
        dialog.setPassword( data.getPassword() );

        getApplication().addPane( dialog );
      }
    }
    else
    {
      final LoginRMIDialog dialog = new LoginRMIDialog();
      dialog.setUrl( data.getRmi() );
      dialog.setUser( data.getUserName() );
      dialog.setPassword( data.getPassword() );

      getApplication().addPane( dialog );
    }
  }

  private void processNamedQuery( final ActionEvent event )
  {
    if ( ! checkSession() ) return;
    final String name = event.getActionCommand().replace( "names:", "" );
    final QueryWindow window = new QueryWindow();
    window.setQuery( getController().getQueries().getQuery( name ) );
    getApplication().addPane( window );
  }

  private void processScript( final ActionEvent event )
  {
    if ( ! checkSession() ) return;
    final String name = event.getActionCommand().replace( "scripts:", "" );

    if ( "manage".equals( name ) )
    {
      getApplication().addPane( new ScriptManagementWindow() );
    }
    else if ( "sample".equals( name ) )
    {
      getApplication().addPane( new ScriptSampleWindow() );
    }
    else
    {
      try
      {
        new ScriptEditorExecuteListener().run( fromFile( name ) );
      }
      catch ( Throwable t )
      {
        getApplication().processFatalException(
            format( "Error executing script: %s", name ), t );
      }
    }
  }

  private void processWorkspace( final ActionEvent event )
  {
    if ( ! checkSession() ) return;
    final String name = event.getActionCommand().replace( "workspaces:", "" );

    if ( "jcr:create".equals( name ) )
    {
      getApplication().addPane( new WorkspaceCreateDialog( false ) );
    }
    else if ( "jcr:clone".equals( name ) )
    {
      getApplication().addPane( new WorkspaceCreateDialog( true ) );
    }
    else
    {
      if ( getController().getWorkspace().getName().equals( name ) ) return;

      Session session = null;

      try
      {
        session = new SessionManager().getSession( getController().getCredentials(), name );
        getController().setSession( session );
      }
      catch ( Throwable t )
      {
        if ( session != null ) session.logout();
        getApplication().processFatalException(
            format( "Error switching to workspace: %s", name ), t );
      }
    }
  }

  private boolean checkSession()
  {
    boolean result = true;

    if ( getApplication().getProperty( Session.class.getName() ) == null )
    {
      result = false;
      getApplication().addPane( new ErrorPane(
          getString( AbstractSessionListener.class, "error.title" ),
          getString( AbstractSessionListener.class, "error.message" ) ) );
    }

    return result;
  }
}
