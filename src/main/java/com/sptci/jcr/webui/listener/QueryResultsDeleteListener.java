package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.NodeDeleteDialog;
import com.sptci.jcr.webui.QueryResultsView;

/**
 * The event listener for displaying the delete dialogue for a node selected
 * in the query/search results view.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-14
 * @version $Id: QueryResultsDeleteListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class QueryResultsDeleteListener extends QueryResultsViewListener
{
  private static final long serialVersionUID = 1L;

  protected void process( final QueryResultsView view )
  {
    final NodeDeleteDialog<QueryResultsView> dialog =
        new NodeDeleteDialog<QueryResultsView>( getNode( view ) );
    dialog.setView( view );
    getApplication().addPane( dialog );
  }
}
