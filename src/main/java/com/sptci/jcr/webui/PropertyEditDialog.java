package com.sptci.jcr.webui;

import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.binding.ViewInitialiser;
import com.sptci.jcr.webui.table.PropertiesTable;
import com.sptci.jcr.webui.table.PropertiesTableModel;

import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import nextapp.echo.app.SplitPane;

import echopoint.Strut;

import static com.sptci.echo.Application.getApplication;
import static nextapp.echo.app.SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP;

import javax.jcr.Property;
import javax.jcr.RepositoryException;

import java.util.Collection;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-17
 * @version $Id: PropertyEditDialog.java 59 2010-02-09 18:07:17Z spt $
 */
public class PropertyEditDialog extends PropertyManagementDialog
{
  private static final long serialVersionUID = 1L;

  private Component type;

  @ActionListener( value = "com.sptci.jcr.webui.listener.PropertySaveListener" )
  private Button save;

  @ActionListener( value = "com.sptci.jcr.webui.listener.PropertyEditDialogNavigationListener" )
  private Button previous;
  @ActionListener( value = "com.sptci.jcr.webui.listener.PropertyEditDialogNavigationListener" )
  private Button next;

  private int index;

  public PropertyEditDialog( final PropertiesTable<PropertiesTableModel> table )
  {
    super( table );
    new ViewInitialiser<PropertyEditDialog>( this ).init();
    setIndex();
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
    
    name.setEnabled( false );
  }

  @Override
  protected Component createControls( final SplitPane spane )
  {
    final Component container = super.createControls( spane );

    container.add( save, 0 );
    container.add( new Strut(), 0 );

    container.add( previous, 0 );

    container.add( new Strut() );
    
    container.add( next );

    return container;
  }

  private void setIndex()
  {
    index = table.getSelectionModel().getMinSelectedIndex();
  }

  private void createContent( final SplitPane spane )
  {
    final Grid grid = new Grid();
    grid.add( nameLabel );

    try
    {
      name.setText( getProperty().getName() );
    }
    catch ( RepositoryException e )
    {
      getApplication().processFatalException( e );
    }

    grid.add( name );

    grid.add( typeLabel );
    grid.add( type );

    createVersionable();
    if ( versionable() )
    {
      grid.add( versionableLabel );
      grid.add( versionable );
    }
    
    grid.add( valueLabel );

    if ( value != null ) grid.add( value );

    spane.add( grid );
  }

  public int getIndex()
  {
    return index;
  }

  public void setIndex( final int index )
  {
    this.index = index;
  }

  public void setValue( final Component value )
  {
    if ( pane != null )
    {
      final Component container = pane.getComponent( 1 );

      if ( this.value != null )
      {
        container.remove( this.value );
      }
    }

    this.value = new ValueContainer( value );
    if ( pane != null ) pane.getComponent( 1 ).add( this.value );
  }

  public void setValues( final Collection<Component> values )
  {
    if ( pane != null )
    {
      final Component container = pane.getComponent( 1 );

      if ( value != null )
      {
        container.remove( value );
      }
    }

    value = null;

    for ( final Component component : values )
    {
      if ( value == null ) value = new ValueContainer( component );
      else value.addValue( component );
    }

    if ( pane != null ) pane.getComponent( 1 ).add( value );
  }

  public Property getProperty()
  {
    return table.getModel().getObjectAt( index );
  }

  public Button getPrevious() { return previous; }
  public Button getNext() { return next; }

  public void setType( final Component type )
  {
    if ( pane != null )
    {
      final Component container = pane.getComponent( 1 );

      if ( this.type != null )
      {
        container.remove( this.type );
      }

      final int idx = container.indexOf( typeLabel ) + 1;
      if ( idx > 0 ) container.add( type, idx );
    }
    
    this.type = type;
  }

  public void enable() { save.setEnabled( true ); }
  public void disable() { save.setEnabled( false ); }
}
