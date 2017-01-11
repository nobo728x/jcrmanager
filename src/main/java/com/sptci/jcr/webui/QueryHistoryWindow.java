package com.sptci.jcr.webui;

import com.sptci.echo.Grid;
import com.sptci.echo.Row;
import com.sptci.echo.WindowPane;
import com.sptci.echo.style.Extent;
import static com.sptci.jcr.webui.MainController.getController;

import echopoint.InfoWindow;
import nextapp.echo.app.Border;
import nextapp.echo.app.Button;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * A window used to display the history of JCR queries that have been
 * executed from the specified
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-09-07
 * @version $Id$
 */
public class QueryHistoryWindow extends WindowPane
{
  private static final long serialVersionUID = 1L;

  private final QueryWindow window;
  private final Listener listener = new Listener();

  public QueryHistoryWindow( final QueryWindow window )
  {
    this.window = window;
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    final Grid grid = new Grid( 1 );
    grid.setBorder( new Border( 1, Color.BLACK, Border.STYLE_SOLID ) );
    grid.setWidth( Extent.getInstance( 100, Extent.PERCENT ) );
    addQueries( grid );
    
    add( grid );
  }

  private void addQueries( final Component parent )
  {
    for ( final String query : getController().getHistory() )
    {
      if ( query.length() > 50 )
      {
        final Row row = new Row();

        final Button button = new Button( query.substring( 0, 40 ) );
        button.setActionCommand( query );
        button.addActionListener( listener );
        button.setStyleName( "Link.Button" );
        row.add( button );

        final InfoWindow label = new InfoWindow();
        label.setContent( query );
        label.setText( "..." );
        row.add( label );

        parent.add( row );
      }
      else
      {
        final Button button = new Button( query );
        button.setActionCommand( query );
        button.addActionListener( listener );
        button.setStyleName( "Link.Button" );
        parent.add( button );
      }
    }
  }

  public QueryWindow getWindow()
  {
    return window;
  }

  private class Listener implements ActionListener
  {
    private static final long serialVersionUID = 1L;

    public void actionPerformed( final ActionEvent event )
    {
      window.setQuery( event.getActionCommand() );
      userClose();
    }
  }
}
