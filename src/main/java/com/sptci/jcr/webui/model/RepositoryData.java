package com.sptci.jcr.webui.model;

import java.io.Serializable;

/**
 * A data bean that represents the data for connection to a repository.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-06
 * @version $Id: RepositoryData.java 73 2011-11-04 17:23:50Z orair $
 */
public class RepositoryData implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String configuration;
  private String directory;
  private final String userName;
  private final String password;
  private String rmi;
  private String jndi;

  public RepositoryData( final String configuration, final String directory,
      final String userName, final String password )
  {
    this.configuration = configuration;
    this.directory = directory;
    this.userName = userName;
    this.password = password;
  }

  public RepositoryData( final String rmi, final String userName,
      final String password )
  {
    this.rmi = rmi;
    this.userName = userName;
    this.password = password;
  }

  public RepositoryData(final boolean jndi, final String urlOrResource, final String userName,
	      final String password ){
	  if (jndi){
		  this.jndi=urlOrResource;
	  }else{
		  this.rmi=urlOrResource;
	  }
	    this.userName = userName;
	    this.password = password;
  }

  
  public String getConfiguration() { return configuration; }

  public String getDirectory() { return directory; }

  public String getUserName() { return userName; }

  public String getPassword() { return password; }

  public String getRmi() { return rmi; }

  public String getJndi() { return jndi; }

}
