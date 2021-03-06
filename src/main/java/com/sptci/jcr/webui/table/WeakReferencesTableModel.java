package com.sptci.jcr.webui.table;

import static com.sptci.jcr.webui.MainController.getController;

import javax.jcr.Node;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;

/**
 * A table model used to represent all the nodes that reference a specified
 * node as weak reference.
 * 
 * <p>&copy; Copyright 2010 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-02-08
 * @version $Id: WeakReferencesTableModel.java 59 2010-02-09 18:07:17Z spt $
 */
public class WeakReferencesTableModel extends SubNodesTableModel
{
  private static final long serialVersionUID = 1L;

  public WeakReferencesTableModel( final Node node )
  {
    super( node );
  }

  @Override
  protected void fetchData()
  {
    data.clear();

    try
    {
      final PropertyIterator iter = node.getWeakReferences();
      iter.skip( page * pageSize );

      int count = 0;
      while ( iter.hasNext() )
      {
        data.add( iter.nextProperty().getParent() );
        if ( count++ > pageSize ) break;
      }

      fireTableDataChanged();
    }
    catch ( Throwable t )
    {
      getController().displayException( t );
    }
  }

  @Override
  protected void setTotalRows()
  {
    try
    {
      totalRows = node.getWeakReferences().getSize();
    }
    catch ( RepositoryException e )
    {
      getController().displayException( e );
    }
  }
}
