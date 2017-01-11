package com.sptci.jcr.webui;

import com.sptci.echo.Column;
import com.sptci.echo.Utilities;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.binding.ViewInitialiser;
import com.sptci.jcr.webui.table.PropertiesTable;
import com.sptci.jcr.webui.table.PropertiesTableModel;

import nextapp.echo.app.Border;
import nextapp.echo.app.Button;
import nextapp.echo.app.Color;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;

import echopoint.Strut;

/**
 * A view that is used to display all the properties for a specified node.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-16
 * @version $Id$
 */
public class PropertiesView extends Column
{
  private static final long serialVersionUID = 1L;

  /** The container used to display the properties. */
  private final PropertiesTable<PropertiesTableModel> properties;

  @ActionListener( value = "com.sptci.jcr.webui.listener.PropertyCreateDialogListener" )
  private Button add;
  @ActionListener( value = "com.sptci.jcr.webui.listener.PropertyEditDialogListener" )
  private Button edit;
  @ActionListener( value = "com.sptci.jcr.webui.listener.PropertyDeleteListener" )
  private Button delete;

  /**
   * @param properties The properties table to use.
   */
  public PropertiesView( final PropertiesTable<PropertiesTableModel> properties )
  {
    this.properties = properties;
    new ViewInitialiser<PropertiesView>( this ).init();
  }

  @Override
  public void init()
  {
    setBorder( new Border( 2, Color.BLACK, Border.STYLE_GROOVE ) );
    removeAll();
    super.init();

    add( createLabel( "title") );
    add( properties );

    add( new Strut() );
    createControls();
  }

  private void createControls()
  {
    final Row controls = new Row();
    controls.add( add );
    controls.add( new Strut() );
    controls.add( edit );
    controls.add( new Strut() );
    controls.add( delete );

    add( controls );
  }

  private Label createLabel( final String key )
  {
    return Utilities.createLabel( getClass().getName(), key, "Bold.Label" );
  }

  public PropertiesTable<PropertiesTableModel> getProperties()
  {
    return properties;
  }
}
