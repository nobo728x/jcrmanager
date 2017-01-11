package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.NodeMoveDialog;
import com.sptci.jcr.webui.QueryResultsView;

/**
 * The event listener for moving a search/query results node to another
 * parent node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-14
 * @version $Id: QueryResultsMoveListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class QueryResultsMoveListener extends QueryResultsViewListener
{
  private static final long serialVersionUID = 1L;

  protected void process( final QueryResultsView view )
  {
    final NodeMoveDialog<QueryResultsView> dialog =
        new NodeMoveDialog<QueryResultsView>( getNode( view ) );
    dialog.setView( view );
    getApplication().addPane( dialog );
  }
}
