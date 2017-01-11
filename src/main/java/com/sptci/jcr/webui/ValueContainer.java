package com.sptci.jcr.webui;

import com.sptci.echo.Add;
import com.sptci.echo.Delete;
import com.sptci.echo.Grid;
import com.sptci.echo.list.BooleanSelectField;

import echopoint.RegexTextField;
import echopoint.jquery.DateField;
import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.Label;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

import java.util.ArrayList;
import java.util.List;
import static java.util.logging.Logger.getAnonymousLogger;

/**
 * A container for displaying editable multi-value properties.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-20
 * @version $Id$
 */
public class ValueContainer extends Grid
{
  private static final long serialVersionUID = 1L;

  private AddListener  addListener = new AddListener();
  private DeleteListener deleteListener = new DeleteListener();

  public ValueContainer( final Component child )
  {
    super( 3 );
    addValue( child );
  }

  public void addValue( final Component value )
  {
    add( value );
    add( createAdd() );

    if ( getComponentCount() > 3 )
    {
      add( createDelete() );
    }
    else
    {
      add( new Label() );
    }
  }

  public List<Component> getValues()
  {
    final List<Component> collection = new ArrayList<Component>();

    for ( int i = 0; i < getComponentCount(); i += 3 )
    {
      collection.add( getComponent( i ) );
    }

    return collection;
  }

  private Component createAdd()
  {
    final Button button = new Add();
    button.addActionListener( addListener );
    return button;
  }

  private Component createDelete()
  {
    final Button button = new Delete();
    button.addActionListener( deleteListener );
    return button;
  }

  protected class AddListener implements ActionListener
  {
    private static final long serialVersionUID = 1L;

    public void actionPerformed( final ActionEvent event )
    {
      final Button button = (Button) event.getSource();
      int index = ValueContainer.this.indexOf( button ) + 1;
      
      ValueContainer.this.add( createComponent(), ++index );
      ValueContainer.this.add( createAdd(), ++index );
      ValueContainer.this.add( createDelete(), ++index );
    }

    private Component createComponent()
    {
      Component component = null;
      final Component source = getComponent( 0 );

      if ( source instanceof RegexTextField )
      {
        component = new RegexTextField( ( (RegexTextField) source ).getRegex() );
      }
      else if ( source instanceof StringComponent )
      {
        final StringComponent sc = new StringComponent();
        sc.setTextContent( ( (StringComponent) source ).isTextContent() );
        component = sc;
      }
      else if ( source instanceof BooleanSelectField )
      {
        final BooleanSelectField sf = new BooleanSelectField();
        final BooleanSelectField sourceSf = (BooleanSelectField) source;
        sf.setWidth( sourceSf.getWidth() );

        component = sf;
      }
      else if ( source instanceof DateField )
      {
        component = new DateField();
      }
      else if ( source instanceof FileUploadComponent )
      {
        component = new FileUploadComponent();
      }
      else
      {
        getAnonymousLogger().warning(
            String.format( "No type for component: %s%n", source ) );
      }

      return component;
    }
  }

  /** The event listener for deleting a value from the view. */
  protected class DeleteListener implements ActionListener
  {
    private static final long serialVersionUID = 1L;

    public void actionPerformed( final ActionEvent event )
    {
      final Button button = (Button) event.getSource();
      int index = ValueContainer.this.indexOf( button );

      for ( int i = 0; i < 3; ++i )
      {
        ValueContainer.this.remove( index - i );
      }
    }
  }
}
