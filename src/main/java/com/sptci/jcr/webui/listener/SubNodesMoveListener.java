package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.NodeMoveDialog;
import com.sptci.jcr.webui.SubNodesView;

/**
 * The event listener for moving a node displayed in {@link
 * com.sptci.jcr.webui.SubNodesView} to a different parent.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-20
 * @version $Id: SubNodesMoveListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class SubNodesMoveListener extends SubNodesViewListener
{
  private static final long serialVersionUID = 1L;

  protected void process( final SubNodesView view )
  {
    final NodeMoveDialog<SubNodesView> dialog =
        new NodeMoveDialog<SubNodesView>( getNode( view ) );
    dialog.setView( view );
    getApplication().addPane( dialog );
  }
}
