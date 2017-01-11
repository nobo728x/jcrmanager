package com.sptci.jcr.webui.model;

import static com.sptci.jcr.Environment.environment;

import com.thoughtworks.xstream.XStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import static java.lang.String.format;
import java.util.Collection;
import static java.util.Collections.unmodifiableCollection;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getAnonymousLogger;

/**
 * A model object used to store named queries for the application.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-10-01
 * @version $Id: NamedQueries.java 41 2009-11-16 20:16:22Z spt $
 */
public class NamedQueries implements Serializable
{
  private static final long serialVersionUID = 1l;

  private static final String FILE_NAME = "queries.xml";

  private Map<String,String> map = new LinkedHashMap<String,String>();

  private NamedQueries() {}

  public static NamedQueries getNamedQueries()
  {
    NamedQueries queries = null;
    final File file = getFile();

    if ( file.exists() )
    {
      try
      {
        queries = (NamedQueries) new XStream().fromXML(
            new BufferedInputStream( new FileInputStream( file ) ) );
      }
      catch ( FileNotFoundException e )
      {
        getAnonymousLogger().log( SEVERE, "Error deserialising named queries", e );
      }
    }

    return ( queries == null ) ? new NamedQueries() : queries;
  }

  public Collection<String> getNames()
  {
    return unmodifiableCollection( map.keySet() );
  }

  public String getQuery( final String name )
  {
    return map.get( name );
  }

  public boolean hasQuery( final String name )
  {
    return map.containsKey( name );
  }

  public synchronized void save( final String name, final String query )
      throws FileNotFoundException
  {
    map.put(  name, query );
    save();
  }

  private void save() throws FileNotFoundException
  {
    final File file = getFile();
    new XStream().toXML( this,
        new BufferedOutputStream( new FileOutputStream( file ) ) );
  }

  private static File getFile()
  {
    return new File( format( "%s/%s", environment.getDataDirectory(), FILE_NAME ) );
  }
}
