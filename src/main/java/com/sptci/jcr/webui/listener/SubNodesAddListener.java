package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.NodeCreateDialog;
import com.sptci.jcr.webui.SubNodesView;

import javax.jcr.Node;

/**
 * The event listener for adding a child node to the node in {@link
 * com.sptci.jcr.webui.SubNodesView}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-20
 * @version $Id: SubNodesAddListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class SubNodesAddListener extends SubNodesViewListener
{
  private static final long serialVersionUID = 1L;

  protected void process( final SubNodesView view )
  {
    final NodeCreateDialog<SubNodesView> dialog =
        new NodeCreateDialog<SubNodesView>( getNode( view ) );
    dialog.setView( view );
    getApplication().addPane( dialog );
  }

  @Override
  protected boolean checkView( final SubNodesView view ) { return true; }

  @Override
  protected Node getNode( final SubNodesView view )
  {
    return ( view.getTable().getSelectionModel().isSelectionEmpty() ) ?
        view.getModel().getNode() : super.getNode( view );
  }
}
