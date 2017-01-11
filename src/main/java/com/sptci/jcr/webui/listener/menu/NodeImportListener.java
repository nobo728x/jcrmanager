package com.sptci.jcr.webui.listener.menu;

import com.sptci.jcr.webui.NodeImportDialog;

import nextapp.echo.app.event.ActionEvent;

/**
 * The event listener for importing data uploaded from a previous export (or
 * another repository) into a specified node.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-24
 * @version $Id: NodeImportListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class NodeImportListener extends NodeManagementListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    if ( checkView() )
    {
      getApplication().addPane( new NodeImportDialog( getNode() ) );
    }
    else
    {
      displayError();
    }
  }
}
