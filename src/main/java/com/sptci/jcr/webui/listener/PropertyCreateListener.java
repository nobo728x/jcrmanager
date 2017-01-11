package com.sptci.jcr.webui.listener;

import com.sptci.echo.ErrorPane;
import com.sptci.echo.Listener;
import com.sptci.echo.list.BooleanSelectField;
import com.sptci.jcr.webui.FileUploadComponent;
import com.sptci.jcr.webui.NodeSelectionComponent;
import com.sptci.jcr.webui.PropertyCreateDialog;
import com.sptci.jcr.webui.StringComponent;
import com.sptci.jcr.webui.model.ValueTypes;

import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.text.TextComponent;

import echopoint.jquery.DateField;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.jcr.webui.MainController.getController;
import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;
import static javax.jcr.PropertyType.PATH;
import static javax.jcr.PropertyType.URI;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * The event listener used to add a property to a node. <p/> <p>&copy;
 * Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans Pareil
 * Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-17
 * @version $Id: PropertyCreateListener.java 63 2010-02-16 15:23:00Z spt $
 */
@SuppressWarnings( { "UnusedDeclaration" } )
public class PropertyCreateListener extends Listener<PropertyCreateDialog>
{
  private static final long serialVersionUID = 1L;

  public void actionPerformed( final ActionEvent event )
  {
    final PropertyCreateDialog dialog = getView( event );

    if ( !checkView( dialog ) )
    {
      final ErrorPane pane = new ErrorPane(
          getString( dialog, "error.title" ),
          getString( dialog, "error.message" ) );
      getApplication().addPane( pane );
      return;
    }

    try
    {
      save( dialog );

      final Node node = dialog.getTable().getModel().getNode();
      dialog.getTable().addRow( node.getProperty( dialog.getName() ) );

      dialog.userClose();
    }
    catch ( Throwable t )
    {
      processFatalException( t );
    }
  }

  @Override
  protected boolean checkView( final PropertyCreateDialog dialog )
  {
    boolean result = super.checkView( dialog );
    final Node node = dialog.getTable().getModel().getNode();

    try
    {
      result = result && ( !node.hasProperty( dialog.getName() ) );
    }
    catch ( RepositoryException e )
    {
      processFatalException( e );
    }

    return result;
  }

  private void save( final PropertyCreateDialog dialog )
      throws RepositoryException
  {
    final ValueTypes type = dialog.getType();
    final Node node = dialog.getTable().getModel().getNode();
    final Session session = getController().getSession();

    if ( getController().isVersionable( node ) && ! node.isCheckedOut() )
    {
      getController().getVersionManager().checkout( node.getPath() );
    }

    switch ( type )
    {
      case Binary:
        setBinary( node, dialog, session );
        break;
      case Boolean:
        setBoolean( node, dialog, session );
        break;
      case Date:
        setDate( node, dialog, session );
        break;
      case Decimal:
        setDecimal( node, dialog, session );
        break;
      case Double:
        setDouble( node, dialog, session );
        break;
      case Long:
        setLong( node, dialog, session );
        break;
      case Path:
        setPath( node, dialog );
        break;
      case WeakReference:
      case Reference:
        setNode( node, dialog, session );
        break;
      case URI:
        setURI( node, dialog, session );
        break;
      default:
        setString( node, dialog, session );
        break;
    }

    session.save();

    if ( dialog.isVersionable() )
    {
      getController().getVersionManager().checkin( node.getPath() );
      getController().displayNode( node );
    }
  }

  private void setBinary( final Node node,
      final PropertyCreateDialog dialog, final Session session )
      throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();

    if ( values.size() == 1 )
    {
      final FileUploadComponent field = (FileUploadComponent) values.get( 0 );
      final ValueFactory factory = session.getValueFactory();
      node.setProperty( dialog.getName(),
          factory.createValue( factory.createBinary( field.getInputStream() ) ) );
      new File( field.getPath() ).delete();
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final FileUploadComponent field = (FileUploadComponent) values.get( i );
      final ValueFactory factory = session.getValueFactory();
      array[i] = factory.createValue( factory.createBinary( field.getInputStream() ) );
      new File( field.getPath() ).delete();
    }

    node.setProperty( dialog.getName(), array );
  }

  private void setBoolean( final Node node,
      final PropertyCreateDialog dialog, final Session session )
      throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();

    if ( values.size() == 1 )
    {
      final BooleanSelectField field =
          (BooleanSelectField) values.get( 0 );
      node.setProperty( dialog.getName(),
          session.getValueFactory().createValue( field.isTrue() ) );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      array[i] = session.getValueFactory().createValue(
          ( (BooleanSelectField) values.get( i ) ).isTrue() );
    }

    node.setProperty( dialog.getName(), array );
  }

  private void setDate( final Node node,
      final PropertyCreateDialog dialog, final Session session )
      throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();

    if ( values.size() == 1 )
    {
      final DateField field = (DateField) values.get( 0 );

      final Calendar calendar = Calendar.getInstance(
          getApplication().getTimeZone() );
      calendar.setTimeInMillis( field.getDate().getTime() );

      node.setProperty( dialog.getName(),
          session.getValueFactory().createValue( calendar ) );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final DateField field = (DateField) values.get( i );

      final Calendar calendar = Calendar.getInstance(
          getApplication().getTimeZone() );
      calendar.setTimeInMillis( field.getDate().getTime() );

      array[i] = session.getValueFactory().createValue( calendar );
    }

    node.setProperty( dialog.getName(), array );
  }

  private void setDecimal( final Node node,
      final PropertyCreateDialog dialog, final Session session )
      throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();

    if ( values.size() == 1 )
    {
      final TextComponent field = (TextComponent) values.get( 0 );
      node.setProperty( dialog.getName(),
          session.getValueFactory().createValue( new BigDecimal( field.getText() ) ) );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final TextComponent field = (TextComponent) values.get( i );
      array[i] = session.getValueFactory().createValue( new BigDecimal( field.getText() ) );
    }

    node.setProperty( dialog.getName(), array );
  }

  private void setDouble( final Node node,
      final PropertyCreateDialog dialog, final Session session )
      throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();

    if ( values.size() == 1 )
    {
      final TextComponent field = (TextComponent) values.get( 0 );
      node.setProperty( dialog.getName(),
          session.getValueFactory().createValue( parseDouble( field.getText() ) ) );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final TextComponent field = (TextComponent) values.get( i );
      array[i] = session.getValueFactory().createValue( parseDouble( field.getText() ) );
    }

    node.setProperty( dialog.getName(), array );
  }

  private void setLong( final Node node,
      final PropertyCreateDialog dialog, final Session session )
      throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();

    if ( values.size() == 1 )
    {
      final TextComponent field = (TextComponent) values.get( 0 );
      node.setProperty( dialog.getName(),
          session.getValueFactory().createValue( parseLong( field.getText() ) ) );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final TextComponent field = (TextComponent) values.get( i );
      array[i] = session.getValueFactory().createValue( parseLong( field.getText() ) );
    }

    node.setProperty( dialog.getName(), array );
  }

  private void setPath( final Node node, final PropertyCreateDialog dialog )
      throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();

    if ( values.size() == 1 )
    {
      final NodeSelectionComponent field = (NodeSelectionComponent) values.get( 0 );
      node.setProperty( dialog.getName(), field.getPath(), PATH );
      return;
    }

    final String[] array = new String[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final NodeSelectionComponent field = (NodeSelectionComponent) values.get( i );
      array[i] = field.getPath();
    }

    node.setProperty( dialog.getName(), array, PATH );
  }

  private void setNode( final Node node, final PropertyCreateDialog dialog,
      final Session session ) throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();

    if ( values.size() == 1 )
    {
      final NodeSelectionComponent field = (NodeSelectionComponent) values.get( 0 );
      final Value value = session.getValueFactory().createValue(
          getController().getNode( field.getPath() ),
          ValueTypes.WeakReference == dialog.getType() );
      node.setProperty( dialog.getName(), value );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final NodeSelectionComponent field = (NodeSelectionComponent) values.get( i );
      array[i] = session.getValueFactory().createValue( field.getNode() );
    }

    node.setProperty( dialog.getName(), array );
  }

  private void setURI( final Node node,
      final PropertyCreateDialog dialog, final Session session )
      throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();

    if ( values.size() == 1 )
    {
      final StringComponent field = (StringComponent) values.get( 0 );
      node.setProperty( dialog.getName(),
          session.getValueFactory().createValue( field.getText(), URI ) );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final StringComponent field = (StringComponent) values.get( i );
      array[i] = session.getValueFactory().createValue( field.getText(), URI );
    }

    node.setProperty( dialog.getName(), array );
  }

  private void setString( final Node node,
      final PropertyCreateDialog dialog, final Session session )
      throws RepositoryException
  {
    final List<Component> values = dialog.getValue().getValues();

    if ( values.size() == 1 )
    {
      final StringComponent field = (StringComponent) values.get( 0 );
      node.setProperty( dialog.getName(), field.getText() );
      return;
    }

    final Value[] array = new Value[values.size()];
    for ( int i = 0; i < array.length; ++i )
    {
      final StringComponent field = (StringComponent) values.get( i );
      array[i] = session.getValueFactory().createValue( field.getText() );
    }

    node.setProperty( dialog.getName(), array );
  }
}
