package com.sptci.jcr.webui.listener.menu;

import com.sptci.jcr.webui.NodeCreateDialog;

import nextapp.echo.app.event.ActionEvent;

/**
 * The event listener used to display a {@link com.sptci.jcr.webui.NodeCreateDialog}
 * in the application. <p/> <p>&copy; Copyright 2009 <a
 * href='http://sptci.com/' target='_new'>Sans Pareil Technologies,
 * Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-13
 * @version $Id: NodeCreateListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class NodeCreateListener extends NodeManagementListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    if ( checkView() )
    {
      final NodeCreateDialog dialog = new NodeCreateDialog( getNode() );
      getApplication().addPane( dialog );
    }
    else
    {
      displayError();
    }
  }

}
