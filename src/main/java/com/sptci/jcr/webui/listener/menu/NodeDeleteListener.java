package com.sptci.jcr.webui.listener.menu;

import com.sptci.jcr.webui.NodeDeleteDialog;

import nextapp.echo.app.event.ActionEvent;

/**
 * The event listener to use to display the dialogue used to delete node(s)
 * from the repository.  By default it will attempt to delete the currently
 * selected node in the content area.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-13
 * @version $Id: NodeDeleteListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class NodeDeleteListener extends NodeManagementListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent actionEvent )
  {
    final NodeDeleteDialog dialog = new NodeDeleteDialog( getNode() );
    getApplication().addPane( dialog );
  }
}
