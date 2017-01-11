package com.sptci.jcr.webui;

import static com.sptci.echo.Application.getApplication;

import nextapp.echo.app.Grid;
import nextapp.echo.app.SplitPane;

/**
 * A dialogue used to capture the login information for accessing a
 * repository configured as a resource in the web application.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-08-19
 * @version $Id: ResourceDialog.java 16 2009-08-20 20:49:33Z spt $
 */
public class ResourceDialog extends LoginDialog
{
  private static final long serialVersionUID = 1l;

  /** The name of the environment resource to use as the repository. */
  private final String resource;

  public ResourceDialog( final String resource ) { this.resource = resource; }

  @Override
  public void init()
  {
    super.init();
    getApplication().setFocusedComponent( user );
  }

  @Override
  protected void createGrid( final SplitPane pane )
  {
    final Grid grid = new Grid();
    grid.add( userLabel );
    grid.add( user );
    grid.add( passwordLabel );
    grid.add( password );
    pane.add( grid );
  }

  public String getResource() { return resource; }
}
