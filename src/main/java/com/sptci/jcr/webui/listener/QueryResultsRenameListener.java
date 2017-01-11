package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.NodeRenameDialog;
import com.sptci.jcr.webui.QueryResultsView;

/**
 * The event listener for renaming a node displayed as part of a query/search
 * results view.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-14
 * @version $Id: QueryResultsRenameListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class QueryResultsRenameListener extends QueryResultsViewListener
{
  private static final long serialVersionUID = 1L;

  @Override
  protected void process( final QueryResultsView view )
  {
    final NodeRenameDialog<QueryResultsView> dialog =
        new NodeRenameDialog<QueryResultsView>( getNode( view ) );
    dialog.setView( view );
    getApplication().addPane( dialog );
  }
}
