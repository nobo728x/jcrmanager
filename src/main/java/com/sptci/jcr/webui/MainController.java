package com.sptci.jcr.webui;

import com.sptci.jcr.webui.model.NamedQueries;
import com.sptci.jcr.webui.model.QueryHistory;
import com.sptci.jcr.webui.model.RepositoryConnectionData;
import com.sptci.jcr.webui.tree.JCRTree;

import static com.sptci.echo.Application.getApplication;
import static com.sptci.jcr.Environment.environment;
import static java.lang.String.format;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getAnonymousLogger;
import static org.apache.jackrabbit.JcrConstants.MIX_VERSIONABLE;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.jcr.Workspace;
import javax.jcr.nodetype.NodeType;
import javax.jcr.version.VersionManager;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * The main view controller for the application.  A utility class for
 * implementing global JCR interactions.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_top'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-06-01
 * @version $Id: MainController.java 72 2011-11-04 17:19:52Z orair $
 */
@SuppressWarnings( { "ClassWithTooManyMethods" } )
public class MainController implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger logger = getAnonymousLogger();

  private QueryHistory history;
  private NamedQueries queries;

  /** Cannot be instantiated.  Use factory method to fetch instance. */
  private MainController() {}

  /** Initialise the main controller. */
  public static void init()
  {
    final MainController controller = new MainController();
    getApplication().setProperty( controller.getClass().getName(), controller );
    
    controller.history = QueryHistory.getHistory();
    getApplication().setProperty( QueryHistory.class.getName(), controller.history );

    controller.queries = NamedQueries.getNamedQueries();
  }

  /** @return The global controller instance. */
  public static MainController getController()
  {
    return (MainController)
        getApplication().getProperty( MainController.class.getName() );
  }

  /** @return The content area for the application. */
  public ContentArea getContentArea()
  {
    return (ContentArea) getApplication().getContentPane();
  }

  /**
   * Set the second child component of the main content area to a node
   * view that displays the specified node.
   *
   * @param node The node whose details are to be displayed.
   */
  public void displayNode( final Node node )
  {
    getContentArea().setChild( new NodeViewController( node ).getView() );
  }

  /** @return The JCR session in use by the application. */
  public Session getSession()
  {
    return (Session) getApplication().getProperty( Session.class.getName() );
  }

  /**
   * Set the current repository session.  If a previous session is current,
   * logout of it, update the property in {@link com.sptci.echo.Application},
   * and update the primary view in {@link ContentArea}.
   *
   * @param session The session to bind to the application.
   */
  public void setSession( final Session session )
  {
        // Get a previows session and logout them if needed
        final Session s = getSession();
        
        if (s != null)
        {
            try
            {
                if(s.isLive()) 
                {
                    s.logout();
                }
            }catch (java.lang.IllegalStateException e){
                logger.log( WARNING, "Unable to logout session", e );
            }
        } 

    // Save the new session inside context
    getApplication().setProperty( Session.class.getName(), session );
    getContentArea().setTree( new JCRTree() );
    getContentArea().resetMenu();
  }

  /** @return The repository to which the application is bound. */
  public Repository getRepository() { return getSession().getRepository(); }

  /** @return Return the workspace to which the current session is associated. */
  public Workspace getWorkspace() { return getSession().getWorkspace(); }

  /** Return the version manager to use to perform version control operations.
   *
   * @return The version manager bound to the default workspace for the current
   *   session.
   * @throws RepositoryException If errors are encountered.
   */
  public VersionManager getVersionManager() throws RepositoryException
  {
    return getSession().getWorkspace().getVersionManager();
  }

  /**
   * @return Return the value factory to use to create property values.
   * @throws RepositoryException If errors are encountered.
   */
  public ValueFactory getValueFactory() throws RepositoryException
  {
    return getSession().getValueFactory();
  }

  /** @return The credentials used to access the repository. */
  public Credentials getCredentials()
  {
    return (Credentials) getApplication().getProperty( Credentials.class.getName() );
  }

  public Node getNode( final String path ) throws RepositoryException
  {
    return (Node) getSession().getItem( path );
  }

  public Collection<String> getResources()
  {
    final Collection<String> collection = new ArrayList<String>();

    try
    {
      final Context context = new InitialContext();
      addBinding( "java:/comp/env/jcr", context, collection );
    }
    catch ( Throwable t )
    {
      getAnonymousLogger().log( FINE, "Error querying for resources", t );
    }

    return collection;
  }

  public boolean isVersionable( final Node node ) throws RepositoryException
  {
    boolean result = false;

    for ( final NodeType type : node.getMixinNodeTypes() )
    {
      if ( type.isMixin() && type.isNodeType( MIX_VERSIONABLE ) )
      {
        result = true;
        break;
      }
    }

    return result;
  }

  public File getUploadDirectory()
  {
    final File directory = new File( "/tmp/sptjcrviewer" );
    
    if ( ! directory.exists() )
    {
      directory.mkdirs();
      directory.deleteOnExit();
    }

    return directory;
  }

  private void addBinding( final String name, final Context environment,
      final Collection<String> collection ) throws NamingException
  {
    for ( NamingEnumeration<Binding> enumeration =
        environment.listBindings( name ); enumeration.hasMore(); )
    {
      final Binding binding = enumeration.nextElement();
      final String resource = format( "%s/%s", name, binding.getName() );

      if ( environment.lookup( resource ) instanceof Repository )
      {
        collection.add( resource );
      }
      else addBinding( resource, environment, collection );
    }
  }

  /** @return The root node in the JCR. */
  public Node getRoot()
  {
    try
    {
      return getSession().getRootNode();
    }
    catch ( RepositoryException e )
    {
      getApplication().processFatalException( e );
    }

    return null;
  }

  /** @return The file in which the saved repository connection data is stored. */
  public String getRepositoryDataFile()
  {
    return environment.getDataDirectory() + "/repositories.xml";
  }

  /** @return The data object with all the saved data or {@code null}. */
  public RepositoryConnectionData getConnectionData()
  {
    RepositoryConnectionData data = null;

    try
    {
      data = RepositoryConnectionData.getConnectionData(
          getRepositoryDataFile() );
    }
    catch ( IOException e )
    {
      getApplication().processFatalException( e );
    }

    return data;
  }

  public void addHistory( final String query ) { history.add( query ); }
  public Collection<String> getHistory() { return history.getQueries(); }

  public NamedQueries getQueries() { return queries; }

  /**
   * Display the exception in a floating window.
   *
   * @param t The exception to display.
   */
  public void displayException( final Throwable t )
  {
    getApplication().processFatalException( t );
  }
}
