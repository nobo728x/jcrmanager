package com.sptci.jcr.webui.table;

import com.sptci.echo.table.ColumnMetaData;
import com.sptci.echo.table.DefaultPageableTableModel;

import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionIterator;

import java.util.ArrayList;

/**
 * A table model used to represent all the versions for a specified node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-30
 * @version $Id: VersionsTableModel.java 59 2010-02-09 18:07:17Z spt $
 */
public class VersionsTableModel extends DefaultPageableTableModel<Version>
{
  private static final long serialVersionUID = 1L;

  @SuppressWarnings( { "TransientFieldNotInitialized" } )
  private final transient Node node;

  public VersionsTableModel( final Node node ) { this.node = node; }

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
    final Version vnode = getObjectAt( row );
    Object value = null;

    try
    {
      switch( column )
      {
        case 0:
          value = vnode.getName();
          break;
        case 1:
          value = getController().getSession().getNodeByIdentifier( vnode.getIdentifier() );
          break;
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
      final VersionIterator vi = node.getSession().getWorkspace().
          getVersionManager().getVersionHistory( node.getPath() ).getAllVersions();
      int count = 0;
      vi.skip( ( page * pageSize ) + 1 );

      while ( vi.hasNext() )
      {
        data.add( vi.nextVersion() );
        if ( ++count > pageSize ) break;
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
    columns.add( new ColumnMetaData( "uuid", Version.class ) );
  }

  protected void setTotalRows()
  {
    try
    {
      totalRows = node.getSession().getWorkspace().getVersionManager().
          getVersionHistory( node.getPath() ).getAllVersions().getSize();
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }
  }

  public Node getNode() { return node; }
}
