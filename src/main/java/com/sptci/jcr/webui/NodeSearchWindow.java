package com.sptci.jcr.webui;

import static com.sptci.echo.Application.getApplication;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Component;
import nextapp.echo.app.Row;
import nextapp.echo.app.TextField;

/**
 * A window used to search for nodes with the specified name.  Typically
 * used to select the desired node from matching results and use for other
 * purposes.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-21
 * @version $Id: NodeSearchWindow.java 18 2009-08-22 11:46:13Z spt $
 */
public class NodeSearchWindow extends AbstractQueryWindow
{
  private static final long serialVersionUID = 1L;

  @Constraints( value = Constraints.Value.NOT_NULL )
  @ActionListener( value = "com.sptci.jcr.webui.listener.QueryExecutionListener" )
  protected TextField query;

  private final SetNodeView view;

  public NodeSearchWindow( final SetNodeView view )
  {
    this.view = view;
    new ViewInitialiser<NodeSearchWindow>( this ).init();
  }

  @Override
  public void init()
  {
    super.init();
    getApplication().setFocusedComponent( query );
  }

  protected Component createTop()
  {
    final Row row = new Row();
    row.add( query );
    row.add( execute );

    return row;
  }

  @Override
  public String getQuery() { return query.getText(); }

  @Override
  public void setQuery( final String query ) { this.query.setText( query ); }

  public SetNodeView getView()
  {
    return view;
  }
}
