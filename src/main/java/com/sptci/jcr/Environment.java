package com.sptci.jcr;

import static com.sptci.io.FileUtilities.FILE_SEPARATOR;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.System.getProperty;

import java.io.Serializable;

/**
 * A singleton that loads and manages application configuration information.
 * Most configurable values are configured as JVM system properties under
 * the {@code sptjcrmanager} root.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-10-28
 * @version $Id: Environment.java 44 2010-01-12 21:01:38Z spt $
 */
public class Environment implements Serializable
{
  private static final long serialVersionUID = 1L;

  /**
   * The default external application directory.
   *
   * {@value}
   */
  public static final String DEFAULT_DATA_DIR = "/var/data/sptjcrmanager";

  /**
   * The default query history limit for the application.
   *
   * {@value}
   */
  private static final String DEFAULT_QUERY_HISTORY_LIMIT = "50";

  /** The singleton instance of the environment properties class. */
  public static final Environment environment = new Environment();

  private Environment() {}

  /**
   * Return the root directory under which persistent data for the application
   * are stored.  Configure via the {@code sptjcrmanager.data.dir} system
   * property.
   *
   * @return The system proeprty value if specified or {@link #DEFAULT_DATA_DIR}
   */
  public String getDataDirectory()
  {
    return getProperty( "sptjcrmanager.data.dir", DEFAULT_DATA_DIR );
  }

  /**
   * Return the maximum number of queries to store in the history buffer.
   * Configure via the {@code sptjcrmanager.query.history} system property.
   *
   * @return The system property value if specified or {@link #DEFAULT_QUERY_HISTORY_LIMIT}.
   */
  public int getQueryHistoryLimit()
  {
    return parseInt( getProperty( "sptjcrmanager.query.history",
        DEFAULT_QUERY_HISTORY_LIMIT ) );
  }

  /** @return The base directory under which groovy scripts are stored. */
  public String getScriptDirectory()
  {
    return format( "%s%sscripts", getDataDirectory(), FILE_SEPARATOR );
  }
}
