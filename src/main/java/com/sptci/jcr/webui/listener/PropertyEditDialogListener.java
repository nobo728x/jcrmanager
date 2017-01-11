package com.sptci.jcr.webui.listener;

import com.sptci.echo.list.BooleanSelectField;
import com.sptci.jcr.webui.FileUploadComponent;
import com.sptci.jcr.webui.NodeSelectionComponent;
import com.sptci.jcr.webui.PropertiesView;
import com.sptci.jcr.webui.PropertyEditDialog;
import com.sptci.jcr.webui.StringComponent;

import nextapp.echo.app.Component;
import nextapp.echo.app.Label;
import nextapp.echo.app.event.ActionEvent;

import echopoint.NumberTextField;
import echopoint.RegexTextField;
import echopoint.jquery.DateField;

import static com.sptci.jcr.webui.MainController.getController;
import static javax.jcr.PropertyType.BINARY;
import static javax.jcr.PropertyType.BOOLEAN;
import static javax.jcr.PropertyType.DATE;
import static javax.jcr.PropertyType.DOUBLE;
import static javax.jcr.PropertyType.LONG;
import static javax.jcr.PropertyType.PATH;
import static javax.jcr.PropertyType.REFERENCE;
import static javax.jcr.PropertyType.nameFromValue;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An event listener for editing the selected properties.  Displays a
 * dialog window that enables users to edit all the properties that were
 * selected.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-17
 * @version $Id: PropertyEditDialogListener.java 68 2011-03-30 19:42:04Z spt $
 */
public class PropertyEditDialogListener extends PropertyManagementListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final PropertiesView view = getView( event );
    final Collection<Property> properties = getProperties( view.getProperties() );

    if ( ! checkSelected( view, properties ) ) return;

    display( view, properties );
  }

  private void display( final PropertiesView view,
      final Collection<Property> properties )
  {
    final PropertyEditDialog dialog = new PropertyEditDialog( view.getProperties() );
    dialog.setIndex( view.getProperties().getSelectionModel().getMinSelectedIndex() );

    dialog.getPrevious().setEnabled( false );
    dialog.getNext().setEnabled( false );

    if ( properties.size() > 1 ) dialog.getNext().setEnabled( true );

    bind( dialog );

    getApplication().addPane( dialog );
  }

  public void bind( final PropertyEditDialog dialog )
  {
    final Property property = dialog.getProperty();

    try
    {
      setType( dialog, property );
      setValue( dialog, property );
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }

  }

  private void setType( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    dialog.setType( new Label( nameFromValue( property.getType() ) ) );
  }

  private void setValue( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    switch ( property.getType() )
    {
      case BINARY:
        createBinary( dialog, property );
        break;
      case BOOLEAN:
        createBoolean( dialog, property );
        break;
      case DATE:
        createDate( dialog, property );
        break;
      case DOUBLE:
        createDouble( dialog, property );
        break;
      case LONG:
        createLong( dialog, property );
        break;
      case PATH:
        createPath( dialog, property );
        break;
      case REFERENCE:
        createNode( dialog, property );
        break;
      default:
        createText( dialog, property );
        break;
    }
  }

  private void createBinary( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    if ( property.getDefinition().isMultiple() )
    {
      final Value[] values = property.getValues();
      final Collection<Component> collection =
          new ArrayList<Component>( values.length );

      for ( final Value value : values )
      {
        collection.add( new FileUploadComponent() );
      }

      dialog.setValues( collection );
      return;
    }

    dialog.setValue( new FileUploadComponent() );
  }

  private void createDouble( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    if ( property.getDefinition().isMultiple() )
    {
      final Value[] values = property.getValues();
      final Collection<Component> collection =
          new ArrayList<Component>( values.length );

      for ( final Value value : values )
      {
        final NumberTextField tf = new NumberTextField();
        tf.setText( value.getString() );
        collection.add( tf );
      }

      dialog.setValues( collection );
      return;
    }

    final NumberTextField tf = new NumberTextField();
    tf.setText( property.getString() );
    dialog.setValue( tf );
  }

  private void createLong( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    if ( property.getDefinition().isMultiple() )
    {
      final Value[] values = property.getValues();
      final Collection<Component> collection =
          new ArrayList<Component>( values.length );

      for ( final Value value : values )
      {
        final RegexTextField tf = new RegexTextField( "^[\\d]+" );
        tf.setText( value.getString() );
        collection.add( tf );
      }

      dialog.setValues( collection );
      return;
    }

    final RegexTextField tf = new RegexTextField( "^[\\d]+" );
    tf.setText( property.getString() );
    dialog.setValue( tf );
  }

  private void createText( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    if ( property.getDefinition().isMultiple() )
    {
      final Value[] values = property.getValues();
      final Collection<Component> collection =
          new ArrayList<Component>( values.length );

      for ( final Value value : values )
      {
        final StringComponent tf =  new StringComponent();
        tf.setText( value.getString() );
        collection.add( tf );
      }

      dialog.setValues( collection );
      return;
    }

    final StringComponent tf =  new StringComponent();
    tf.setText( property.getString() );

    dialog.setValue( tf );
  }

  private void createBoolean( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    if ( property.getDefinition().isMultiple() )
    {
      final Value[] values = property.getValues();
      final Collection<Component> collection =
          new ArrayList<Component>( values.length );

      for ( final Value value : values )
      {
        final boolean val = value.getBoolean();
        final BooleanSelectField field = new BooleanSelectField();
        field.setSelectedIndex( ( val ) ? 0 : 1 );
        collection.add( field );
      }

      dialog.setValues( collection );
      return;
    }

    final boolean value = property.getBoolean();
    final BooleanSelectField field = new BooleanSelectField();
    field.setSelectedIndex( ( value ) ? 0 : 1 );

    dialog.setValue( field );
  }

  private void createDate( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    if ( property.getDefinition().isMultiple() )
    {
      final Value[] values = property.getValues();
      final Collection<Component> collection =
          new ArrayList<Component>( values.length );

      for ( final Value value : values )
      {
        collection.add( new DateField( value.getDate() ) );
      }

      dialog.setValues( collection );
      return;
    }

    dialog.setValue( new DateField( property.getDate() ) );
  }

  private void createPath( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    if ( property.getDefinition().isMultiple() )
    {
      final Value[] values = property.getValues();
      final Collection<Component> collection =
          new ArrayList<Component>( values.length );

      for ( final Value value : values )
      {
        collection.add( new NodeSelectionComponent(
            getController().getNode( value.getString() ) ) );
      }

      dialog.setValues( collection );
      return;
    }

    dialog.setValue( new NodeSelectionComponent(
        getController().getNode( property.getString() ) ) );
  }

  private void createNode( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    if ( property.getDefinition().isMultiple() )
    {
      final Value[] values = property.getValues();
      final Collection<Component> collection =
          new ArrayList<Component>( values.length );

      for ( final Value value : values )
      {
        collection.add( new NodeSelectionComponent(
            getController().getSession().getNodeByIdentifier( value.getString() ) ) );
      }

      dialog.setValues( collection );
      return;
    }

    dialog.setValue( new NodeSelectionComponent( property.getNode() ) );
  }
}
