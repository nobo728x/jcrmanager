package com.sptci.jcr.webui.listener;

import com.sptci.echo.list.BooleanSelectField;
import com.sptci.jcr.webui.FileUploadComponent;
import com.sptci.jcr.webui.NodeSelectionComponent;
import com.sptci.jcr.webui.PropertyEditDialog;
import com.sptci.jcr.webui.StringComponent;

import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.text.TextComponent;

import echopoint.jquery.DateField;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;
import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.ValueFormatException;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * The event listener for saving the property displayed in a {@link
 * PropertyEditDialog}.
 *
 *<p>&copy; Copyright 2009 <a
 * href='http://sptci.com/' target='_new'>Sans Pareil Technologies,
 * Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-18
 * @version $Id: PropertySaveListener.java 63 2010-02-16 15:23:00Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class PropertySaveListener extends AbstractPropertyEditListener
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final PropertyEditDialog dialog = getView( event );
    final Property property = dialog.getProperty();

    try
    {
      checkout( dialog );
      bind( dialog, property );
      save( dialog );
      finish( dialog, property );
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }
  }

  private void checkout( final PropertyEditDialog dialog )
      throws RepositoryException
  {
    final Node node = dialog.getTable().getModel().getNode();

    if ( getController().isVersionable( node ) && ! node.isCheckedOut() )
    {
      getController().getVersionManager().checkout( node.getPath() );
    }
  }

  private void save( final PropertyEditDialog dialog ) throws RepositoryException
  {
    getController().getSession().save();

    if ( dialog.isVersionable() )
    {
      getController().getVersionManager().checkin(
          dialog.getTable().getModel().getNode().getPath() );
      getController().displayNode( dialog.getTable().getModel().getNode() );
    }
  }

  private void checkProperty( final PropertyEditDialog dialog,
      final Property property, final Collection<Component> values )
      throws RepositoryException
  {
    if ( ! property.getDefinition().isMultiple() )
    {
      if ( values.size() > 1 )
      {
        throw new ValueFormatException( getString( dialog, "error.multivalue" ) );
      }
    }
  }

  private void bind( final PropertyEditDialog dialog, final Property property )
      throws RepositoryException
  {
    switch ( property.getType() )
    {
      case PropertyType.BINARY:
        setBinary( dialog, property );
        break;
      case PropertyType.BOOLEAN:
        setBoolean( dialog, property );
        break;
      case PropertyType.DATE:
        setDate( dialog, property );
        break;
      case PropertyType.DECIMAL:
        setDecimal( dialog, property );
        break;
      case PropertyType.DOUBLE:
        setDouble( dialog, property );
        break;
      case PropertyType.LONG:
        setLong( dialog, property );
        break;
      case PropertyType.PATH:
        setPath( dialog, property );
        break;
      case PropertyType.WEAKREFERENCE:
      case PropertyType.REFERENCE:
        setNode( dialog, property );
        break;
      case PropertyType.URI:
        setURI( dialog, property );
        break;
      case PropertyType.STRING:
        setText( dialog, property );
        break;
    }
  }

  private void setBinary( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();
    checkProperty( dialog, property, values );

    final ValueFactory factory = getController().getValueFactory();

    if ( ! property.getDefinition().isMultiple() )
    {
      final FileUploadComponent field =
          (FileUploadComponent) values.get( 0 );
      property.setValue( factory.createBinary( field.getInputStream() ) );
      new File( field.getPath() ).delete();
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final FileUploadComponent field =
          (FileUploadComponent) values.get( i );
      array[i] = factory.createValue( factory.createBinary( field.getInputStream() ) );
      new File( field.getPath() ).delete();
    }

    property.setValue( array );
  }

  private void setBoolean( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();
    checkProperty( dialog, property, values );

    if ( ! property.getDefinition().isMultiple() )
    {
      property.setValue(
          ( (BooleanSelectField) values.get( 0 ) ).isTrue() );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final boolean val = ( (BooleanSelectField) values.get( i ) ).isTrue();
      array[i] = getController().getValueFactory().createValue( val );
    }

    property.setValue( array );
  }

  private void setDate( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();
    checkProperty( dialog, property, values );

    if ( ! property.getDefinition().isMultiple() )
    {
      final Date date = ( (DateField) values.get( 0 ) ).getDate();
      final Calendar calendar = Calendar.getInstance( getApplication().getTimeZone() );
      calendar.setTimeInMillis( date.getTime() );
      property.setValue( calendar );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final Date date = ( (DateField) values.get( i ) ).getDate();
      final Calendar calendar = Calendar.getInstance( getApplication().getTimeZone() );
      calendar.setTimeInMillis( date.getTime() );
      array[i] = getController().getValueFactory().createValue( calendar );
    }

    property.setValue( array );
  }

  private void setDecimal( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();
    checkProperty( dialog, property, values );

    if ( ! property.getDefinition().isMultiple() )
    {
      property.setValue(
          new BigDecimal( ( (TextComponent) values.get( 0 ) ).getText() ) );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final BigDecimal val =
          new BigDecimal( ( (TextComponent) values.get( i ) ).getText() );
      array[i] = getController().getValueFactory().createValue( val );
    }

    property.setValue( array );
  }

  private void setDouble( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();
    checkProperty( dialog, property, values );

    if ( ! property.getDefinition().isMultiple() )
    {
      property.setValue(
          parseDouble( ( (TextComponent) values.get( 0 ) ).getText() ) );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final double val =
          parseDouble( ( (TextComponent) values.get( i ) ).getText() );
      array[i] = getController().getValueFactory().createValue( val );
    }

    property.setValue( array );
  }

  private void setLong( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();
    checkProperty( dialog, property, values );

    if ( ! property.getDefinition().isMultiple() )
    {
      property.setValue( parseLong( ( (TextComponent) values.get( 0 ) ).getText() ) );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final double val =
          parseLong( ( (TextComponent) values.get( i ) ).getText() );
      array[i] = getController().getValueFactory().createValue( val );
    }

    property.setValue( array );
  }

  private void setPath( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();
    checkProperty( dialog, property, values );

    if ( ! property.getDefinition().isMultiple() )
    {
      final NodeSelectionComponent field =
          ( (NodeSelectionComponent) dialog.getValue().getValues().get( 0 ) );
      property.setValue( field.getPath() );
      return;
    }

    final String[] array = new String[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final NodeSelectionComponent field =
          ( (NodeSelectionComponent) dialog.getValue().getValues().get( i ) );
      array[i] = field.getPath();
    }

    property.setValue( array );
  }

  private void setNode( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();
    checkProperty( dialog, property, values );

    if ( ! property.getDefinition().isMultiple() )
    {
      final NodeSelectionComponent field =
          ( (NodeSelectionComponent) dialog.getValue().getValues().get( 0 ) );
      final Value value = getController().getValueFactory().createValue(
          field.getNode(), PropertyType.WEAKREFERENCE == property.getType() );
      property.setValue( value );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final NodeSelectionComponent field =
          ( (NodeSelectionComponent) dialog.getValue().getValues().get( i ) );
      array[i] = getController().getValueFactory().createValue(
          field.getNode(), PropertyType.WEAKREFERENCE == property.getType() );
    }

    property.setValue( array );
  }

  private void setURI( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();
    checkProperty( dialog, property, values );

    if ( ! property.getDefinition().isMultiple() )
    {
      final String text = ( (StringComponent) values.get( 0 ) ).getText();
      final Value value =
          getController().getValueFactory().createValue( text, PropertyType.URI );
      property.setValue( value );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final String val = ( (StringComponent) values.get( i ) ).getText();
      array[i] =
          getController().getValueFactory().createValue( val, PropertyType.URI );
    }

    property.setValue( array );
  }

  private void setText( final PropertyEditDialog dialog,
      final Property property ) throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();
    checkProperty( dialog, property, values );

    if ( ! property.getDefinition().isMultiple() )
    {
      property.setValue(
          ( (StringComponent) dialog.getValue().getValues().get( 0 ) ).getText() );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final String val = ( (StringComponent) values.get( i ) ).getText();
      array[i] = getController().getValueFactory().createValue( val );
    }

    property.setValue( array );
  }

  private void finish( final PropertyEditDialog dialog,
      final Property property )
  {
    dialog.getTable().updateRow( dialog.getIndex(), property );

    if ( getIndices( dialog ).size() > 1 )
    {
      dialog.disable();
    }
    else
    {
      dialog.userClose();
    }
  }
}
