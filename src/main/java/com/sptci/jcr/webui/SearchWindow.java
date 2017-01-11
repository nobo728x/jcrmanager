package com.sptci.jcr.webui;

import static com.sptci.echo.Application.getApplication;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Component;
import nextapp.echo.app.Row;
import nextapp.echo.app.TextField;

/**
 * The window used to display a search clause input area and button to perform a search
 * across the entire repository for the clause.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-08-12
 * @version $Id: SearchWindow.java 10 2009-08-15 23:08:37Z spt $
 */
public class SearchWindow extends AbstractQueryWindow
{
  private static final long serialVersionUID = 1l;

  @Constraints( value = Constraints.Value.NOT_NULL )
  @ActionListener( value = "com.sptci.jcr.webui.listener.QueryExecutionListener" )
  protected TextField query;

  public SearchWindow()
  {
    new ViewInitialiser<SearchWindow>( this ).init();
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
}
