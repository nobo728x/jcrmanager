package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.NodeCreateDialog;
import com.sptci.jcr.webui.QueryResultsView;

/**
 * The event listener for adding a new node under a selected node in the
 * {@link com.sptci.jcr.webui.QueryResultsView}.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-20
 * @version $Id: QueryResultsAddListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class QueryResultsAddListener extends QueryResultsViewListener
{
  private static final long serialVersionUID = 1L;

  protected void process( final QueryResultsView view )
  {
    final NodeCreateDialog<QueryResultsView> dialog =
        new NodeCreateDialog<QueryResultsView>( getNode( view ) );
    getApplication().addPane( dialog );
  }
}
