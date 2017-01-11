package com.sptci.jcr;

import static com.sptci.ReflectionUtility.execute;
import static com.sptci.io.FileUtilities.FILE_SEPARATOR;
import static com.sptci.jcr.Environment.environment;
import static java.lang.String.format;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getAnonymousLogger;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.xml.NodeTypeReader;
import org.apache.jackrabbit.rmi.repository.RMIRemoteRepository;
import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.apache.jackrabbit.spi.QNodeTypeDefinition;

import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.nodetype.NodeTypeManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Logger;

/**
 * Manages a JCR session.  Use to login to a repository identified by its
 * configuration file and location. <p/> <p>&copy; Copyright 2009 <a
 * href='http://sptci.com/' target='_top'>Sans Pareil Technologies,
 * Inc.</a></p>
 *
 * @author Rakesh 2009-06-01
 * @version $Id: SessionManager.java 83 2013-07-24 19:33:50Z spt $
 */
public class SessionManager
{
  private static final Logger logger = getAnonymousLogger();
  private Session session;
  private static Repository repository;
  private static String resource;

  /**
   * Create a new session on the specified repository.  If a session has already
   * been opened, logout of that session and login to create a new session.
   *
   * @param file The configuration file for the repository.
   * @param directory The location of the repository.
   * @param user The user name to use to login to the repository.
   * @param password The password to use to login to the repository.
   * @return The new session to the repository.
   * @throws RepositoryException If errors are encountered.
   * @throws IOException If errors are encountered.
   */
  public Session getSession( final String file, final String directory,
      final String user, final String password ) throws RepositoryException, IOException
  {
    if ( session != null ) session.logout();
    shutdown();

    login( file, directory, user, password );
    return session;
  }

  private void login( final String file, final String directory, final String user,
      final String password ) throws IOException, RepositoryException
  {
    shutdown();
    repository = new TransientRepository( file, directory );
    session = repository.login( new SimpleCredentials(
        user, password.toCharArray() ) );
    registerNodeTypes( session );
    logger.info( "Initialised session for user: " + user );
  }

  public void shutdown()
  {
    if ( repository != null )
    {
      try
      {
        execute( repository, "shutdown" );
      }
      catch ( Throwable t )
      {
        logger.log( SEVERE, "Unable to shutdown repository", t );
      }
    }
  }

  /**
   * Return a session initialised through a JNDI resource configured for
   * the web application.
   *
   * @param name The fully qualified resource name (eg. java:comp/env/jcr/repository)
   * @param user The user to login as.
   * @param password The password for the user.
   * @return The session to the repository configured as a JNDI resource.
   */
  public Session getSession( final String name, final String user,
      final String password )
  {
    shutdown();
    resource = name;
    InitialContext context = null;

    try
    {
      context = new InitialContext();
      final Repository repo = (Repository) context.lookup( name );
      session = repo.login(
          new SimpleCredentials( user, password.toCharArray() ) );
      registerNodeTypes( session );
    }
    catch ( Throwable t )
    {
      logger.log( SEVERE, "Unable to login to JNDI resource repository", t );
    }
    finally
    {
      close( context );
    }

    return session;
  }

  /**
   * Return a session to the specified RMI URL.
   *
   * @param url The RMI URL to use to access the repository.
   * @param user The user to login as.
   * @param password The password for the user.
   * @return The session to the remote repository.
   * @throws javax.jcr.RepositoryException If errors are encountered.
   * @throws java.net.MalformedURLException If the URL cannot be parsed.
   */
  public Session getRemoteSession( final String url, final String user,
      final String password ) throws RepositoryException, MalformedURLException
  {
    if ( session != null ) session.logout();
    shutdown();

    repository = url.startsWith( "http://" ) ?
        new URLRemoteRepository( url ) : new RMIRemoteRepository( url );
    session = repository.login( new SimpleCredentials(
        user, password.toCharArray() ) );
    registerNodeTypes( session );
    logger.info( "Initialised remote session for user: " + user );

    return session;
  }

  private void close( final InitialContext context )
  {
    try
    {
      if ( context != null ) context.close();
    }
    catch ( NamingException e )
    {
      logger.log( SEVERE, "Unable to close initial context", e );
    }
  }

  /**
   * Return a new session using either an existing repository or a
   * environment resource.  Typically used to execute background jobs using
   * a separate session.  Please note that callers <b>must</b> logout of
   * the session obtained from this method invocation.
   *
   * @param credentials The credentials to use to login to the repository.
   * @return The new session instance to the repository.
   * @throws RepositoryException If errors are encountered while accessing
   *   the repository.
   * @throws NamingException If errors are encountered if accessing the
   *   environment resource.
   */
  public Session getSession( final Credentials credentials )
      throws RepositoryException, NamingException
  {
    Session sess = null;
    final Repository rep = getRepository();

    if ( rep != null )
    {
      sess = rep.login( credentials );
      registerNodeTypes( sess );
    }

    return sess;
  }

  /**
   * Return a new session using either an existing repository or a
   * environment resource.  Typically used to execute background jobs using
   * a separate session.  Please note that callers <b>must</b> logout of
   * the session obtained from this method invocation.
   *
   * @param credentials The credentials to use to login to the repository.
   * @param workspace The workspace to bind the session to.
   * @return The new session instance to the repository.
   * @throws RepositoryException If errors are encountered while accessing
   *   the repository.
   * @throws NamingException If errors are encountered if accessing the
   *   environment resource.
   */
  public Session getSession( final Credentials credentials, final String workspace )
      throws RepositoryException, NamingException
  {
    Session sess = null;
    final Repository rep = getRepository();

    if ( rep != null )
    {
      sess = rep.login( credentials, workspace );
      registerNodeTypes( sess );
    }

    return sess;
  }

  /** @return The session that is currently open or {@code null}. */
  public Session getSession() { return session; }

  /**
   * Return the repository that is currently open.  This may be a repository
   * opened explicitly by the user, or a repository opened by the container.
   * 
   * @return The repository that is currently open or {@code null}.
   * @throws NamingException If errors are encountered while looking up
   *   the named resource.
   */
  public Repository getRepository() throws NamingException
  {
    Repository rep = null;

    if ( repository != null )
    {
      rep = repository;
    }
    else if ( resource != null )
    {
      final InitialContext context = new InitialContext();

      try
      {
        rep = (Repository) context.lookup( resource );
      }
      finally
      {
        close( context );
      }
    }

    return rep;
  }

  private void registerNodeTypes( final Session sess )
  {
    registerNodeTypesXML( sess );
  }

  private void registerNodeTypesXML( final Session sess )
  {
    final File dir = new File( format( "%s%s%s%s%s",
        environment.getDataDirectory(), FILE_SEPARATOR, "nodetypes",
        FILE_SEPARATOR, "xml" ) );
    if ( ! dir.exists() ) return;

    try
    {
      final NodeTypeManager manager = sess.getWorkspace().getNodeTypeManager();
      final NodeTypeRegistry registry = ( (NodeTypeManagerImpl) manager ).getNodeTypeRegistry();

      for ( final File file : dir.listFiles() )
      {
        logger.info( format( "Loading node types from file: %s", file.getAbsolutePath() ) );

        final QNodeTypeDefinition[] definitions = NodeTypeReader.read( new FileInputStream( file ) );

        for ( final QNodeTypeDefinition def : definitions )
        {
          if ( registry.isRegistered( def.getName() ) ) continue;

          logger.info( format( "Registering node type %s from file: %s",
              def.getName(), file.getAbsolutePath() ) );
          registry.registerNodeType( def );
        }
      }
    }
    catch ( Throwable t )
    {
      logger.log( FINE, "Error loading XML node types", t );
    }
  }
}
