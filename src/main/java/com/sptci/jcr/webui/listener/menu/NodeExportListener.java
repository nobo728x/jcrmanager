package com.sptci.jcr.webui.listener.menu;

import com.sptci.jcr.webui.NodeExportDialog;

import nextapp.echo.app.event.ActionEvent;

import javax.jcr.Node;

/**
 * The event listener to display the dialog using which users may specify the
 * options for exporting a selected node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-24
 * @version $Id: NodeExportListener.java 51 2010-01-26 21:51:13Z spt $
 */
public class NodeExportListener extends NodeManagementListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    if ( checkView() )
    {
      export( getNode() );
    }
    else
    {
      displayError();
    }
  }

  public void export( final Node node )
  {
    getApplication().addPane( new NodeExportDialog( node ) );
  }
}
