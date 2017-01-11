package com.sptci.jcr.webui;

import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.binding.ViewInitialiser;
import com.sptci.echo.list.BooleanSelectField;
import com.sptci.echo.list.EnumListModel;
import com.sptci.jcr.webui.model.ValueTypes;
import com.sptci.jcr.webui.table.PropertiesTable;
import com.sptci.jcr.webui.table.PropertiesTableModel;

import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import nextapp.echo.app.SelectField;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.event.ActionEvent;

import echopoint.NumberTextField;
import echopoint.RegexTextField;
import echopoint.jquery.DateField;

import static com.sptci.echo.Application.getApplication;
import static nextapp.echo.app.SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP;

/**
 * A dialogue used to create a property under a node.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-17
 * @version $Id: PropertyCreateDialog.java 63 2010-02-16 15:23:00Z spt $
 */
public class PropertyCreateDialog extends PropertyManagementDialog
{
  private static final long serialVersionUID = 1L;

  private SelectField type;

  @ActionListener( value = "com.sptci.jcr.webui.listener.PropertyCreateListener" )
  private Button create;

  public PropertyCreateDialog( final PropertiesTable<PropertiesTableModel> table )
  {
    super( table );
    new ViewInitialiser<PropertyCreateDialog>( this ).init();
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    pane = new SplitPane( ORIENTATION_VERTICAL_BOTTOM_TOP );
    pane.setAutoPositioned( true );

    createControls( pane );
    createContent( pane );

    add( pane );
    getApplication().setFocusedComponent( name );
  }

  @Override
  protected Component createControls( final SplitPane spane )
  {
    final Component container = super.createControls( spane );

    container.add( create, 0 );

    return container;
  }

  private Component createText()
  {
    return new StringComponent();
  }

  private void createType()
  {
    type = new com.sptci.echo.list.SelectField<EnumListModel<ValueTypes>>();
    type.setModel( new EnumListModel<ValueTypes>( ValueTypes.class ) );
    type.addActionListener( new TypeListener() );
  }

  private void createContent( final SplitPane spane )
  {
    final Grid grid = new Grid();
    grid.add( nameLabel );
    grid.add( name );
    grid.add( typeLabel );

    createType();
    grid.add( type );

    createVersionable();
    if ( versionable() )
    {
      grid.add( versionableLabel );
      grid.add( versionable );
    }

    grid.add( valueLabel );
    value = new ValueContainer( createText() );
    grid.add( value );

    spane.add( grid );
  }

  public ValueTypes getType() { return (ValueTypes) type.getSelectedItem(); }

  protected class TypeListener implements nextapp.echo.app.event.ActionListener
  {
    private static final long serialVersionUID = 1L;

    public void actionPerformed( final ActionEvent event )
    {
      final ValueTypes typeValue = (ValueTypes) type.getSelectedItem();

      if ( value != null )
      {
        final Component child = pane.getComponent( 1 );
        final int count = child.getComponentCount();
        child.remove( count - 1 );
      }

      switch ( typeValue )
      {
        case Binary:
          value = new ValueContainer( createFileUpload() );
          break;
        case Boolean:
          value = new ValueContainer( createBoolean() );
          break;
        case Date:
          value = new ValueContainer( createDateField() );
          break;
        case Decimal:
        case Double:
          value = new ValueContainer( createDouble() );
          break;
        case Long:
          value = new ValueContainer( createLong() );
          break;
        case Path:
        case WeakReference:
        case Reference:
          value = new ValueContainer( createNode() );
          break;
        default:
          value = new ValueContainer( createText() );
          break;
      }

      pane.getComponent( 1 ).add( value );
    }

    private Component createBoolean()
    {
      return new BooleanSelectField();
    }

    private Component createDateField()
    {
      return new DateField();
    }

    private Component createLong()
    {
      return new RegexTextField( "^[\\d]+" );
    }

    private Component createDouble()
    {
      return new NumberTextField();
    }

    private Component createNode()
    {
      return new NodeSelectionComponent();
    }

    private Component createFileUpload()
    {
      return new FileUploadComponent();
    }
  }
}
