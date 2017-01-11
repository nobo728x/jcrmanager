package com.sptci.jcr.webui.listener;

import com.sptci.echo.Application;
import com.sptci.echo.ErrorPane;
import com.sptci.jcr.SessionManager;
import com.sptci.jcr.webui.NodeImportDialog;
import com.sptci.jcr.webui.tree.AbstractNode;
import com.sptci.jcr.webui.tree.JCRNode;
import com.sptci.jcr.webui.tree.RepositoryTreeModel;

import nextapp.echo.app.TaskQueueHandle;
import nextapp.echo.app.event.ActionEvent;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getAnonymousLogger;
import static javax.jcr.ImportUUIDBehavior.IMPORT_UUID_COLLISION_REMOVE_EXISTING;
import static javax.jcr.ImportUUIDBehavior.IMPORT_UUID_COLLISION_REPLACE_EXISTING;
import static javax.jcr.ImportUUIDBehavior.IMPORT_UUID_COLLISION_THROW;
import static javax.jcr.ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.naming.NamingException;

import java.io.InputStream;

/**
 * The event listener for importing the file uploaded using {@link
 * com.sptci.jcr.webui.NodeImportDialog} under the path specified in the dialogue.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-08-25
 * @version $Id: NodeImportListener.java 51 2010-01-26 21:51:13Z spt $
 */
public class NodeImportListener extends NodeManagementListener<NodeImportDialog>
{
  private static final long serialVersionUID = 1L;
  private TaskQueueHandle taskQueue;
  private Application application;
  private ErrorPane message;

  public void actionPerformed( final ActionEvent event )
  {
    final NodeImportDialog dialog = getView( event );
    final int behaviour = getBehaviour( dialog );
    application = getApplication();
    taskQueue = application.createTaskQueue();

    try
    {
      new Importer( createSession(), dialog.getNode(),
          dialog.getUpload().getInputStream(), behaviour ).start();
      dialog.userClose();
      message = createPane( getString( this, "import.title" ),
          getString( this, "import.message" ) );
      getApplication().addPane( message );
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }
  }

  private Session createSession() throws NamingException, RepositoryException
  {
    return new SessionManager().getSession( getController().getCredentials() );
  }

  private ErrorPane createPane( final String title, final String message )
  {
    final ErrorPane pane = new ErrorPane( title, message );
    pane.setTitle( "Information" );
    return pane;
  }

  private ErrorPane createPane( final String title, final String message,
      final Throwable throwable )
  {
    final ErrorPane pane = new ErrorPane( title, message, throwable );
    pane.setTitle( "Information" );
    return pane;
  }

  private int getBehaviour( final NodeImportDialog dialog )
  {
    int behaviour;

    switch ( dialog.getBehaviour() )
    {
      case Remove:
        behaviour = IMPORT_UUID_COLLISION_REMOVE_EXISTING;
        break;
      case Replace:
        behaviour = IMPORT_UUID_COLLISION_REPLACE_EXISTING;
        break;
      case Error:
        behaviour = IMPORT_UUID_COLLISION_THROW;
        break;
      default:
        behaviour = IMPORT_UUID_CREATE_NEW;
        break;
    }

    return behaviour;
  }

  /** A thread used to import the data into the repository in the background */
  private class Importer extends Thread
  {
    private final Session session;
    private final Node node;
    private final InputStream stream;
    private final int behaviour;

    private Importer( final Session session, final Node node,
        final InputStream stream, final int behaviour )
    {
      this.session = session;
      this.node = node;
      this.stream = stream;
      this.behaviour = behaviour;
    }

    @Override
    public void run()
    {
      try
      {
        session.importXML( node.getPath(), stream, behaviour );
        session.save();
        application.enqueueTask( taskQueue, new Updater( node ) );
      }
      catch ( Throwable t )
      {
        getAnonymousLogger().log( SEVERE, "Error importing data", t );
        application.enqueueTask( taskQueue, new Error( t ) );
      }
      finally
      {
        session.logout();
      }
    }
  }

  /** A runnable instance to push updates to the client after import. */
  private class Updater implements Runnable
  {
    private final Node node;

    public Updater( final Node node ) {this.node = node;}

    public void run()
    {
      displaySuccess();

      final RepositoryTreeModel model =
          getController().getContentArea().getTree().getModel();
      final JCRNode child = getNode( node );
      final AbstractNode<Node> parent = getParent( node );

      try
      {
        if ( ( child != null ) &&
            ! "/".equals( child.getUserObject().getPath() ) )
        {
          model.removeChild( parent, child, node.getPath() );
          model.addChild( parent, new JCRNode( node ) );
        }
        else
        {
          displayError( null );
        }
      }
      catch ( Throwable t )
      {
        displayError( t );
      }
      finally
      {
        message.userClose();
        application.removeTaskQueue( taskQueue );
      }
    }

    private void displayError( final Throwable t )
    {
      if ( t == null )
      {
        application.addPane( createPane(
            getString( NodeImportListener.this, "error.title" ),
            getString( NodeImportListener.this, "error.message" ) ) );
      }
      else
      {
        application.addPane( createPane(
            getString( NodeImportListener.this, "error.title" ),
            getString( NodeImportListener.this, "error.message" ), t ) );
      }
    }

    private void displaySuccess()
    {
      application.addPane( createPane(
          getString( NodeImportListener.this, "success.title" ),
          getString( NodeImportListener.this, "success.message" ) ) );
    }
  }

  /** A runnable instance to push an error message to client. */
  private class Error implements Runnable
  {
    private final Throwable throwable;

    public Error( final Throwable t ) { throwable = t; }

    public void run()
    {
      message.userClose();
      application.processFatalException( throwable );
      application.removeTaskQueue( taskQueue );
    }
  }
}
