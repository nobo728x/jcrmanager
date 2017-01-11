package com.sptci.jcr.webui.table;

import com.sptci.echo.table.SortableHeaderCellRenderer;
import com.sptci.echo.table.SortableTable;
import static com.sptci.echo.table.SortableTableModel.Direction.ascending;
import com.sptci.echo.table.Table;

import static nextapp.echo.app.list.ListSelectionModel.MULTIPLE_SELECTION;
import nextapp.echo.app.table.TableCellRenderer;

import javax.jcr.Property;

/**
 * A table used to display the properties of the specified node.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @param <M> The table model used with this table.
 * @author Rakesh Vidyadharan 2009-08-16
 * @version $Id: PropertiesTable.java 22 2009-08-26 14:42:03Z spt $
 */
public class PropertiesTable<M extends AbstractPropertiesTableModel>
    extends Table<Property> implements SortableTable
{
  private static final long serialVersionUID = 1L;
  private final PropertyCellRenderer renderer = new PropertyCellRenderer();

  public PropertiesTable( final M model )
  {
    super( model );
  }

  @Override
  public TableCellRenderer getDefaultRenderer( final Class aClass )
  {
    return renderer;
  }

  @Override
  @SuppressWarnings( {"unchecked"} )
  public M getModel()
  {
    return (M) super.getModel();
  }

  @Override
  public void init()
  {
    super.init();
    getSelectionModel().setSelectionMode( MULTIPLE_SELECTION );
    setDefaultHeaderRenderer( new SortableHeaderCellRenderer() );
    getModel().sort( 0, ascending );
  }
}