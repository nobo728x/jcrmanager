package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.NodeDeleteDialog;
import com.sptci.jcr.webui.SubNodesView;

/**
 * The event listener for deleting a sub-node in the {@link
 * com.sptci.jcr.webui.SubNodesView} component.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-20
 * @version $Id: SubNodesDeleteListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class SubNodesDeleteListener extends SubNodesViewListener
{
  private static final long serialVersionUID = 1L;

  protected void process( final SubNodesView view )
  {
    final NodeDeleteDialog<SubNodesView> dialog =
        new NodeDeleteDialog<SubNodesView>( getNode( view ) );
    dialog.setView( view );
    getApplication().addPane( dialog );
  }
}
