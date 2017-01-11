package com.sptci.jcr.webui.table;

import com.sptci.echo.table.ColumnMetaData;
import com.sptci.echo.table.DefaultPageableTableModel;

import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

import java.util.ArrayList;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-13
 * @version $Id: NodesTableModel.java 66 2010-02-22 15:24:18Z spt $
 */
public abstract class NodesTableModel extends DefaultPageableTableModel<Node>
{
  @Override
  public void init()
  {
    super.init();

    setTotalRows();
    setColumns();
    fetchData();
  }

  /** {@inheritDoc} */
  public void setPage( final int page )
  {
    this.page = page;
    fetchData();
  }

  /** {@inheritDoc} */
  public void setPageSize( final int pageSize )
  {
    this.pageSize = pageSize;
    this.page = 0;
    fetchData();
  }

  /** {@inheritDoc} */
  @Override
  public Object getValueAt( final int column, final int row )
  {
    final Node node = getObjectAt( row );
    String value = null;

    try
    {
      switch( column )
      {
        case 0:
          value = node.getName();
          break;
        case 1:
          value = node.getPath();
          break;
        case 2:
          value = node.getIdentifier();
      }
    }
    catch ( Throwable t )
    {
      getController().displayException( t );
    }

    return value;
  }

  /**
   * Fetch the data for the current {@link #page} from the data store.
   *
   * @see #setPageSize
   * @see #setPage
   * @see nextapp.echo.app.table.AbstractTableModel#fireTableDataChanged
   */
  protected void fetchData()
  {
    data.clear();

    try
    {
      final NodeIterator iter = getNodes();
      final long skip = page * pageSize;
      iter.skip( skip );

      int count = 0;
      while ( iter.hasNext() )
      {
        data.add( iter.nextNode() );
        if ( ++count >= pageSize ) break;
      }

      fireTableDataChanged();
    }
    catch ( Throwable t )
    {
      getController().displayException( t );
    }
  }

  protected void setColumns()
  {
    columns = new ArrayList<ColumnMetaData>( 2 );
    columns.add( new ColumnMetaData( "name", String.class ) );
    columns.add( new ColumnMetaData( "path", String.class ) );
    columns.add( new ColumnMetaData( "uuid", String.class ) );
  }

  protected void setTotalRows()
  {
    try
    {
      totalRows = getNodes().getSize();
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }
  }

  /**
   * @return Return the fresh iterator over the nodes to be displayed.
   * @throws javax.jcr.RepositoryException If errors are encountered.
   */
  protected abstract NodeIterator getNodes() throws RepositoryException;
}
