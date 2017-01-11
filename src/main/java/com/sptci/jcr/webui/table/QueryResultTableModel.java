package com.sptci.jcr.webui.table;

import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.QueryResult;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-11
 * @version $Id: QueryResultTableModel.java 66 2010-02-22 15:24:18Z spt $
 */
public class QueryResultTableModel extends NodesTableModel
{
  private static final long serialVersionUID = 1L;

  /** The query result that represents the results of a JCR query execution. */
  @SuppressWarnings( { "TransientFieldNotInitialized" } )
  private final transient QueryResult results;

  public QueryResultTableModel( final QueryResult results )
  {
    this.results = results;
  }

  @Override
  protected NodeIterator getNodes() throws RepositoryException
  {
    return results.getNodes();
  }

  @Override
  protected void setTotalRows()
  {
    super.setTotalRows();
    if ( totalRows >= 0 ) return;

    try
    {
      for ( final NodeIterator iter = getNodes(); iter.hasNext(); )
      {
        iter.next();
        ++totalRows;
      }
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }
  }
}
