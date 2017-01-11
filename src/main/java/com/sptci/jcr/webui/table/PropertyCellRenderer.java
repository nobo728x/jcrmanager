package com.sptci.jcr.webui.table;

import com.sptci.echo.style.Extent;
import com.sptci.jcr.webui.NodeButton;
import com.sptci.jcr.webui.listener.NodeListener;

import nextapp.echo.app.Button;
import nextapp.echo.app.Column;
import nextapp.echo.app.Component;
import nextapp.echo.app.Label;
import nextapp.echo.app.Table;
import nextapp.echo.app.table.TableCellRenderer;

import echopoint.HtmlLabel;
import echopoint.InfoWindow;
import echopoint.tucana.DownloadButton;
import echopoint.tucana.InputStreamDownloadProvider;

import static com.sptci.echo.Dimensions.getInt;
import static com.sptci.jcr.webui.MainController.getController;
import static java.lang.String.format;
import static javax.jcr.PropertyType.BINARY;
import static javax.jcr.PropertyType.PATH;
import static javax.jcr.PropertyType.REFERENCE;
import static javax.jcr.PropertyType.STRING;
import static javax.jcr.PropertyType.WEAKREFERENCE;
import static javax.jcr.PropertyType.nameFromValue;
import static org.apache.jackrabbit.JcrConstants.JCR_CONTENT;
import static org.apache.jackrabbit.JcrConstants.JCR_MIMETYPE;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import java.io.IOException;

/**
 * A cell renderer used to display custom components for property values.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-16
 * @version $Id: PropertyCellRenderer.java 59 2010-02-09 18:07:17Z spt $
 */
public class PropertyCellRenderer implements TableCellRenderer
{
  private static final long serialVersionUID = 1L;

  private final NodeListener nodeListener = new NodeListener();

  public Component getTableCellRendererComponent( final Table table,
      final Object value, final int column, final int row )
  {
    final Property property = (Property) value;
    Component component = null;

    try
    {
      switch ( column )
      {
        case 1:
          component = new Label( nameFromValue( property.getType() ) );
          component.setStyleName( "Italic.Label" );
          break;
        case 2:
          component = getValue( property );
          break;
        default:
          component = new Label( property.getName() );
          component.setStyleName( "Bold.Label" );
          break;
      }
    }
    catch ( Throwable t )
    {
      getController().displayException( t );
    }

    return component;
  }

  private Component getValue( final Property property )
      throws RepositoryException, IOException
  {
    final Component component;

    switch ( property.getType() )
    {
      case BINARY:
        component = extractBinary( property );
        break;
      case PATH:
        component = extractPath( property );
        break;
      case WEAKREFERENCE:
      case REFERENCE:
        component = extractReference( property );
        break;
      case STRING:
        component = extractString( property );
        break;
      default:
        component = extractText( property );
        break;
    }

    return component;
  }

  private Component extractString( final Property property )
      throws RepositoryException
  {
    final Component component;

    if ( property.getDefinition().isMultiple() )
    {
      component = new Column();

      for ( final Value value : property.getValues() )
      {
        component.add( createTextComponent( property, value.getString() ) );
      }
    }
    else
    {
      component = createTextComponent( property, property.getString() );
    }

    return component;
  }

  private Component createTextComponent( final Property property,
      final String text ) throws RepositoryException
  {
    final Component component;
    final int length = getInt( this, "value.maxlength" );

    if ( text.length() > length )
    {
      final InfoWindow window = new InfoWindow();
      window.setWidth( Extent.getInstance( 500 ) );
      window.setTitle( format( "Property: %s", property.getName() ) );
      window.setPrefix( text.substring( 0, length ) );
      window.setText( "..." );
      window.setContent( text );

      component = window;
    }
    else
    {
      component = new HtmlLabel( text );
    }

    return component;
  }

  private Component extractText( final Property property ) throws RepositoryException
  {
    final Component component;

    if ( property.getDefinition().isMultiple() )
    {
      component = new Column();

      for ( final Value value : property.getValues() )
      {
        component.add( new HtmlLabel( value.getString() ) );
      }
    }
    else
    {
      component = new HtmlLabel( property.getString() );
    }

    return component;
  }

  private Component extractBinary( final Property property )
      throws RepositoryException
  {
    final Component component;

    if ( property.getDefinition().isMultiple() )
    {
      final Column column = new Column();

      for ( final Value value : property.getValues() )
      {
        final DownloadButton button = new DownloadButton( value.getBinary().getStream() );
        button.setText( button.getProvider().getFileName() );
        button.setStyleName( "Link.Button" );
        column.add( button );
      }

      component = column;
    }
    else
    {
      final DownloadButton button = new DownloadButton( property.getBinary().getStream() );

      if ( JCR_CONTENT.equals( property.getParent().getName() ) )
      {
        final String name = property.getParent().getParent().getName();

        final InputStreamDownloadProvider provider =
            (InputStreamDownloadProvider) button.getProvider();
        provider.setFileName( name );
        provider.setContentType(
            property.getParent().getProperty( JCR_MIMETYPE ).getString() );

        button.setText( name );
      }
      else
      {
        button.setText( button.getProvider().getFileName() );
      }

      button.setStyleName( "Link.Button" );
      component = button;
    }

    return component;
  }

  private Component extractPath( final Property property )
      throws RepositoryException
  {
    final Component component;

    if ( property.getDefinition().isMultiple() )
    {
      final Column column = new Column();

      for ( final Value value : property.getValues() )
      {
        final Node node =
            getController().getNode( value.getString() );
        final Button button = createButton( node );
        column.add( button );
      }

      component = column;
    }
    else
    {
      final String path = property.getString();
      final Node node = getController().getNode( path );

      component = createButton( node );
    }

    return component;
  }

  private Component extractReference( final Property property )
      throws RepositoryException
  {
    final Component component;

    if ( property.getDefinition().isMultiple() )
    {
      final Column column = new Column();

      for ( final Value value : property.getValues() )
      {
        final Node node =
            getController().getSession().getNodeByIdentifier( value.getString() );
        final Button button = createButton( node );
        button.setText( value.getString() );
        column.add( button );
      }

      component = column;
    }
    else
    {
      final Node node = property.getNode();
      final Button button = createButton( node );
      button.setText( node.getIdentifier() );
      component = button;
    }

    return component;
  }

  private Button createButton( final Node node )
  {

    return new NodeButton( node, nodeListener );
  }
}
