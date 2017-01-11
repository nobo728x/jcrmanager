package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.QueryResultsView;
import com.sptci.jcr.webui.listener.menu.NodeExportListener;

/**
 * The listener for exporting the node selected in a search/query results
 * table.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-11
 * @version $Id: QueryResultsExportListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class QueryResultsExportListener extends QueryResultsViewListener
{
  private static final long serialVersionUID = 1L;

  @Override
  protected void process( final QueryResultsView view )
  {
    new NodeExportListener().export( getNode( view ) );
  }
}
