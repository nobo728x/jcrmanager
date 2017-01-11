package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.PropertyEditDialog;
import com.sptci.jcr.webui.table.PropertiesTable;

import java.util.ArrayList;

/**
 * A base listener class for actions triggered from a property edit dialog.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-01
 * @version $Id: AbstractPropertyEditListener.java 26 2009-09-07 22:30:10Z spt $
 */
public abstract class AbstractPropertyEditListener extends Listener<PropertyEditDialog>
{
  protected ArrayList<Integer> getIndices( PropertyEditDialog dialog )
  {
    final PropertiesTable table = dialog.getTable();

    final int min = table.getSelectionModel().getMinSelectedIndex();
    final int max = table.getSelectionModel().getMaxSelectedIndex();
    final ArrayList<Integer> indices = new ArrayList<Integer>();

    for ( int i = min; i <= max; ++i )
    {
      if ( table.getSelectionModel().isSelectedIndex( i ) )
      {
        indices.add( i );
      }
    }

    return indices;
  }
}
