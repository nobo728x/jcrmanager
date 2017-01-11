package com.sptci.jcr.webui.listener;

import com.sptci.jcr.webui.NodeImportDialog;
import com.sptci.jcr.webui.QueryResultsView;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-11
 * @version $Id: QueryResultsImportListener.java 47 2010-01-21 20:38:45Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class QueryResultsImportListener extends QueryResultsViewListener
{
  private static final long serialVersionUID = 1L;

  protected void process( final QueryResultsView view )
  {
    getApplication().addPane( new NodeImportDialog( getNode( view ) ) );
  }
}
