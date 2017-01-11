package com.sptci.jcr.webui.listener;

import com.sptci.echo.Listener;
import com.sptci.jcr.webui.PropertiesView;
import com.sptci.jcr.webui.PropertyCreateDialog;
import com.sptci.jcr.webui.table.PropertiesTable;
import com.sptci.jcr.webui.table.PropertiesTableModel;

import nextapp.echo.app.event.ActionEvent;

/**
 * The event listener for adding a property to a node as triggerd from a
 * {@link PropertiesView}.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-16
 * @version $Id: PropertyCreateDialogListener.java 22 2009-08-26 14:42:03Z spt $
 */
public class PropertyCreateDialogListener extends Listener<PropertiesView>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final PropertiesView view = getView( event );
    final PropertiesTable<PropertiesTableModel> table = view.getProperties();

    getApplication().addPane( new PropertyCreateDialog( table ) );
  }
}
