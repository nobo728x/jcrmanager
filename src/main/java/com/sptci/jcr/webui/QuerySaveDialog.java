package com.sptci.jcr.webui;

import com.sptci.echo.WindowPane;
import com.sptci.echo.annotation.ActionListener;
import com.sptci.echo.annotation.Constraints;
import com.sptci.echo.binding.ViewInitialiser;

import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.TextField;

/**
 * A dialog used to capture the name to assign to a query the user wishes
 * to save.
 *
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh Vidyadharan 2009-10-01
 * @version $Id: QuerySaveDialog.java 63 2010-02-16 15:23:00Z spt $
 */
public class QuerySaveDialog extends WindowPane
{
  private static final long serialVersionUID = 1L;

  private Label nameLabel;
  @ActionListener( value = "com.sptci.jcr.webui.listener.QuerySaveListener" )
  @Constraints( value = Constraints.Value.NOT_NULL )
  private TextField name;

  private final String query;

  public QuerySaveDialog( final String query )
  {
    this.query = query;
    new ViewInitialiser<QuerySaveDialog>( this ).init();
  }

  @Override
  public void init()
  {
    removeAll();
    super.init();

    final Row row = new Row();
    row.add( nameLabel );
    row.add( name );
    add( row );

    getApplicationInstance().setFocusedComponent( name );
  }

  public String getQuery() { return query; }
  public String getName() { return name.getText(); }
}
