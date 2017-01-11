package com.sptci.jcr.webui.table;

import com.sptci.echo.table.ColumnMetaData;
import com.sptci.echo.table.DefaultSortableTableModel;

import static com.sptci.jcr.webui.MainController.getController;
import static java.util.Collections.reverse;
import static javax.jcr.PropertyType.nameFromValue;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * A base table model for displaying node properties (typically via a
 * property iterator).
 *
 * <p>&copy; Copyright 2010 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2010-02-18
 * @version $Id: AbstractPropertiesTableModel.java 66 2010-02-22 15:24:18Z spt $
 */
public abstract class AbstractPropertiesTableModel
    extends DefaultSortableTableModel<Property>
{
  @Override
  public void init()
  {
    setColumns();
    populate();
  }

  /**
   * Over-ridden to always return the property for the row.  The cell
   * renderer is responsible for displaying the appropriate component for
   * the appropriate column (cell for a row).
   *
   * @return The property for the row.
   */
  @Override
  public Object getValueAt( final int column, final int row )
  {
    return getObjectAt( row );
  }

  @Override
  public void sort( final int column, final Direction direction )
  {
    sortIndex = column;
    sortDirection = ( direction == null ) ? Direction.ascending : direction;

    final TreeMap<String, Collection<Property>> map = new TreeMap<String,Collection<Property>>();
    final int size = data.size();

    for ( int i = 0; i < size; ++i )
    {
      final String  value = getStringValue( sortIndex, i );

      if ( ! map.containsKey( value ) )
      {
        map.put( value, new ArrayList<Property>() );
      }

      map.get( value ).add( getObjectAt( i ) );
    }

    data.clear();

    for ( final Map.Entry<String,Collection<Property>> entry : map.entrySet() )
    {
      data.addAll( entry.getValue() );
    }

    if ( Direction.descending == direction ) reverse( data );
    fireTableDataChanged();
  }

  protected void setColumns()
  {
    columns = new ArrayList<ColumnMetaData>( 2 );
    columns.add( new ColumnMetaData( "name", String.class ) );
    columns.add( new ColumnMetaData( "type", String.class ) );
    columns.add( new ColumnMetaData( "value", String.class ) );
  }

  protected String getStringValue( final int column, final int row )
  {
    final Property property = getObjectAt( row );
    String value = "";

    try
    {
      switch ( column )
      {
        case 1:
          value = nameFromValue( property.getType() );
          break;
        case 2:
          value = getText( property );
          break;
        default:
          value = property.getName();
          break;
      }
    }
    catch ( Throwable t )
    {
      getController().displayException( t );
    }

    return value;
  }

  protected String getText( final Property property )
      throws RepositoryException
  {
    final String text;

    if ( property.getDefinition().isMultiple() )
    {
      final StringBuilder builder = new StringBuilder( 128 );

      for ( final Value value : property.getValues() )
      {
        builder.append( value.getString() ).append( '|' );
      }

      text = builder.toString();
    }
    else
    {
      text = property.getString();
    }

    return text;
  }

  protected abstract void populate();
}
