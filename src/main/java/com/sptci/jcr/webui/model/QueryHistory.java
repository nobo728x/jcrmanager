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
 * A simple buffer used to maintain the history of queries executed by the
 * user.  Specify the {@code sptjcrmanager.query.history} system property
 * with the desired buffer size.  Defaults to 50.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-30
 * @version $Id: QueryHistory.java 41 2009-11-16 20:16:22Z spt $
 */
public class QueryHistory implements Serializable
{
  private static final long serialVersionUID = 1l;

  /** The file to which the instance will be serialised. */
  private static final String FILE_NAME = "history.xml";

  /** The maximum number of queries to be held in history. */
  private int maxEntries;

  /** The LRU used to store the queries. */
  private LRU lru;

  private QueryHistory()
  {
    this( environment.getQueryHistoryLimit() );
  }

  private QueryHistory( final int maxEntries )
  {
    this.maxEntries = maxEntries;
    lru = new LRU( maxEntries );
  }

  public static QueryHistory getHistory()
  {
    final File file = getFile();
    QueryHistory history = null;

    if ( file.exists() )
    {
      try
      {
        history = (QueryHistory) new XStream().fromXML(
            new BufferedInputStream( new FileInputStream( file ) ) );
      }
      catch ( FileNotFoundException e )
      {
        getAnonymousLogger().log( SEVERE, "Error deserialising query history", e );
      }
    }

    return ( history == null ) ? new QueryHistory() : history;
  }

  public synchronized void add( final String query )
  {
    lru.put( query, "" );
  }

  public synchronized void remove( final String query )
  {
    lru.remove( query );
  }

  public Collection<String> getQueries()
  {
    return unmodifiableCollection( lru.keySet() );
  }

  public synchronized void save() throws FileNotFoundException
  {
    final File file = getFile();
    new XStream().toXML( this,
        new BufferedOutputStream( new FileOutputStream( file ) ) );
  }

  private static File getFile()
  {
    return new File( format( "%s/%s", environment.getDataDirectory(), FILE_NAME ) );
  }

  private class LRU extends LinkedHashMap<String,String>
  {
    private static final long serialVersionUID = 1L;

    private LRU( final int size )
    {
      super( size, 1.0f, true );
    }

    @Override
    protected boolean removeEldestEntry( final Map.Entry<String,String> eldest )
    {
      return size() > maxEntries;
    }
  }
}
