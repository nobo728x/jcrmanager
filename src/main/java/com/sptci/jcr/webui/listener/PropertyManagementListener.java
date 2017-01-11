package com.sptci.jcr.webui.listener;

import static com.sptci.echo.Configuration.getString;
import com.sptci.echo.ErrorPane;
import com.sptci.echo.Listener;
import com.sptci.jcr.webui.PropertiesView;
import com.sptci.jcr.webui.table.PropertiesTable;

import javax.jcr.Property;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A base event listener for managing properties of a node.  Provides common
 * methods for edit and delete operations.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-17
 * @version $Id: PropertyManagementListener.java 15 2009-08-18 20:00:14Z spt $
 */
public abstract class PropertyManagementListener extends Listener<PropertiesView>
{
  protected boolean checkSelected( final PropertiesView view,
      final Collection<Property> properties )
  {
    boolean result = true;

    if ( properties.size() == 0 )
    {
      result = false;

      final ErrorPane pane = new ErrorPane(
          getString( view, "error.title" ),
          getString( view, "error.message" ) );
      getApplication().addPane( pane );
    }

    return result;
  }

  protected Collection<Property> getProperties( final PropertiesTable table )
  {
    final ArrayList<Property> properties = new ArrayList<Property>();
    if ( table.getSelectionModel().isSelectionEmpty() ) return properties;

    int min = table.getSelectionModel().getMinSelectedIndex();
    int max = table.getSelectionModel().getMaxSelectedIndex();

    for ( int i = min; i <= max; ++i )
    {
      if ( table.getSelectionModel().isSelectedIndex( i ) )
      {
        properties.add( table.getModel().getObjectAt( i ) );
      }
    }

    return properties;
  }
}
