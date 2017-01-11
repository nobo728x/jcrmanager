package com.sptci.jcr.webui.model;

import static com.sptci.io.FileUtilities.mkdirs;
import com.thoughtworks.xstream.XStream;
import static java.util.Collections.unmodifiableCollection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.TreeMap;

/**
 * A java bean that captures saved repository connection details for the
 * user.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-06
 * @version $Id: RepositoryConnectionData.java 63 2010-02-16 15:23:00Z spt $
 */
public class RepositoryConnectionData implements Serializable
{
  private static final long serialVersionUID = 1L;

  private final TreeMap<String,RepositoryData> connections =
      new TreeMap<String,RepositoryData>();

  private RepositoryConnectionData() {}

  /**
   * Return the saved connection data from the specified file.  If the
   * file does not eixst, then a new instance is returned.
   * 
   * @param file The fully qualified path for the file in which the
   *   repository connection data is saved.
   * @return The connection data object with all the saved connections.
   * @throws IOException If errors are encountered while de-serialising
   *   the {@code file}.
   */
  public static RepositoryConnectionData getConnectionData( final String file )
      throws IOException
  {
    final File f = new File( file );

    return f.exists() ? (RepositoryConnectionData) new XStream().fromXML(
        new BufferedInputStream( new FileInputStream( f ) ) ) :
        new RepositoryConnectionData();
  }

  /**
   * Check to see if the saved connections has a connection with the
   * specified {@code name}.
   *
   * @param name The name to assign to the saved data.
   * @return Returns {@code true} if the name already exists.
   */
  public boolean hasName( final String name )
  {
    return connections.containsKey( name );
  }

  /**
   * Add the specified data to the connections map.  If a connection
   * exists with the specified {@code name}, then it is replaced.
   *
   * @param name The name to assign to the saved data.
   * @param data The data to save.
   */
  public void addData( final String name, final RepositoryData data )
  {
    connections.put( name, data );
  }

  /**
   * Remove the connection data with the specified name from the saved
   * connections.
   *
   * @param name The name of the saved connection.
   * @return Return {@code true} if the saved connection was removed.
   */
  public boolean remove( final String name )
  {
    boolean result = false;

    if ( connections.containsKey( name ) )
    {
      connections.remove( name );
      result = true;
    }

    return result;
  }

  /**
   * Return a read-only collection of all the names with saved repository
   * connection information.
   *
   * @return The collection of names.
   */
  public Collection<String> getNames()
  {
    return unmodifiableCollection( connections.keySet() );
  }

  /**
   * Return the repository data object for the specified saved name.
   *
   * @param name The name under which the repository data is saved.
   * @return The repository connection data or {@code null} if no such
   *   saved name exists.
   */
  public RepositoryData getRepositoryData( final String name )
  {
    return connections.get( name );
  }

  /**
   * Save the connection data in this object to the specified file.
   *
   * @param file The fully qualified name of the file to save to.
   * @throws IOException If errors are encountered.
   */
  public void save( final String file ) throws IOException
  {
    final File f = new File( file );
    mkdirs( f );
    new XStream().toXML( this,
        new BufferedOutputStream( new FileOutputStream( f ) ) );
  }
}
