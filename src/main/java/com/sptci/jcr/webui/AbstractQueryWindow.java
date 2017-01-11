package com.sptci.jcr.webui;

import static com.sptci.echo.Configuration.getString;
import static com.sptci.echo.Dimensions.getExtent;
import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;

import echopoint.HtmlLabel;
import nextapp.echo.app.Button;
import nextapp.echo.app.Component;
import nextapp.echo.app.SplitPane;
import static nextapp.echo.app.SplitPane.ORIENTATION_VERTICAL;

/**
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-12
 * @version $Id: AbstractQueryWindow.java 18 2009-08-22 11:46:13Z spt $
 */
public abstract class AbstractQueryWindow extends WindowPane
{
  protected SplitPane pane;
  @ActionListener( value = "com.sptci.jcr.webui.listener.QueryExecutionListener" )
  protected Button execute;

  @Override
  public void init()
  {
    removeAll();
    super.init();

    pane = new SplitPane( ORIENTATION_VERTICAL, getExtent( this, "separatorPosition" ) );
    pane.setResizable( true );

    pane.add( createTop() );
    setResults( new HtmlLabel( getString( this, "results.label" ) ) );

    add( pane );
  }

  public Component getResults()
  {
    Component result = null;
    if ( pane.getComponentCount() > 1 ) result = pane.getComponent( 1 );
    return result;
  }

  public void setResults( final Component child )
  {
    if ( pane.getComponentCount() > 1 ) pane.remove( 1 );
    if ( child != null ) pane.add( child );
  }

  protected abstract Component createTop();

  /** @return The query text as entered in the text area. */
  public abstract String getQuery();

  /**
   * Set the query text to be displayed in the query text area.
   *
   * @param query The query text to display
   */
  public abstract void setQuery( final String query );
}
